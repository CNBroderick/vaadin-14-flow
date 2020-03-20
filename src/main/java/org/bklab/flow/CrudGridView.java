/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-03-20 13:31:13
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：CrudGridView
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.function.ValueProvider;
import dataq.core.jdbc.DBAccess;
import dataq.core.operation.AbstractOperation;
import dataq.core.operation.JdbcQueryOperation;
import org.bklab.flow.component.HorizontalPageBar;
import org.bklab.flow.component.HorizontalRule;
import org.bklab.flow.dialog.ExceptionDialog;
import org.bklab.flow.factory.ButtonFactory;
import org.bklab.flow.factory.ComboBoxFactory;
import org.bklab.flow.factory.TextFieldFactory;
import org.bklab.flow.menu.GridMenuBuilder;
import org.bklab.flow.menu.IGridMenuManager;
import org.bklab.util.PagingList;
import org.bklab.util.search.common.KeyWordSearcher;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("UnusedReturnValue")
public class CrudGridView<T> extends TmbView<CrudGridView<T>> {

    private final HorizontalPageBar<T> pageBar;
    private final List<Consumer<List<T>>> reloadedListeners = new ArrayList<>();
    private final Map<String, Predicate<T>> afterQueryPredicates = new HashMap<>();
    private final Map<String, Supplier<Object>> parameterMap = new HashMap<>();
    private AbstractOperation queryOperation;
    private int singlePageSize = 20;
    private final PagingList<T> pagingList = new PagingList<>(singlePageSize);
    private List<T> entities = new ArrayList<>();
    private Grid<T> grid = new Grid<>();
    private Function<Integer, String> totalDataSizeFormatter = i -> "共" + i + "条数据";
    private final TextField keyword = new TextFieldFactory().placeholder("关键字(按回车可进行搜索)")
            .setClearButtonVisible(true).keyUpEnterListener(e -> doLocalQuery()).lumoSmall().width("15em").get();
    private final Button search = new ButtonFactory().text("查询").icon(VaadinIcon.SEARCH.create())
            .minWidth100px().clickListener(e -> doQuery()).lumoSmall().get();
    private ValueProvider<T, Collection<T>> subEntitiesProvider = null;


    {
        addToolBarRight(keyword, search);
        pageBar = new HorizontalPageBar<>(pagingList);
    }

    public CrudGridView() {
    }

    public CrudGridView(Class<? extends T> clz) {
    }

    @Override
    public CrudGridView<T> title(String title) {
        super.title(title);
        return this;
    }

    public CrudGridView<T> peek(Consumer<CrudGridView<T>> crudGridViewConsumer) {
        crudGridViewConsumer.accept(this);
        return this;
    }

    public CrudGridView<T> setTotalDataSizeFormatter(Function<Integer, String> totalDataSizeFormatter) {
        this.totalDataSizeFormatter = totalDataSizeFormatter;
        return this;
    }

    public CrudGridView<T> setQueryOperation(AbstractOperation operation) {
        this.queryOperation = operation;
        return this;
    }

    public CrudGridView<T> setQueryOperation(Class<? extends JdbcQueryOperation> operationClass, DBAccess db) {
        try {
            this.queryOperation = operationClass.getDeclaredConstructor().newInstance()
                    .setDBAccess(db).setOperationName(operationClass.getSimpleName());
        } catch (Exception e) {
            handleException(e);
        }
        return this;
    }

    public CrudGridView<T> hasAddButton(BiConsumer<ClickEvent<Button>, CrudGridView<T>> consumer) {
        addToolBarRight(new ButtonFactory().text("新建").icon(VaadinIcon.PLUS.create())
                .lumoSmall().minWidth("100px").clickListener(e -> consumer.accept(e, this)).get());
        return this;
    }

    public CrudGridView<T> hasAddButton(ComponentEventListener<ClickEvent<Button>> listener) {
        addToolBarRight(new ButtonFactory().text("新建").icon(VaadinIcon.PLUS.create())
                .lumoSmall().minWidth("100px").clickListener(listener).get());
        return this;
    }

    public void doLocalQuery() {
        doLocalQuery(new ArrayList<>());
    }

    @SafeVarargs
    public final void doLocalQuery(Predicate<T>... predicates) {
        doLocalQuery(Arrays.asList(predicates));
    }

    public void doLocalQuery(List<Predicate<T>> predicates) {
        String key = keyword.getValue();
        Stream<T> stream = entities.stream();
        if (!key.isBlank()) {
            stream = entities.stream().filter(e -> new KeyWordSearcher<>(e).match(key));
        }

        if (!predicates.isEmpty()) {
            stream = entities.stream().filter(e -> predicates.stream().allMatch(p -> p.test(e)));
        }

        List<T> collect = stream.collect(Collectors.toList());

        pagingList.update(collect);
        grid.setItems(collect);
        pagingList.update(collect, singlePageSize);
        pageBar.setOnePageSize(singlePageSize).setTotalDataSizeFormatter(totalDataSizeFormatter).build();
    }

    public CrudGridView<T> doQuery() {
        Map<String, Object> map = new HashMap<>();
        parameterMap.forEach((k, v) -> Optional.ofNullable(v).flatMap(e ->
                Optional.ofNullable(e.get())).ifPresent(m -> map.put(k, m)));
        return doQuery(queryOperation, map);
    }

    public CrudGridView<T> doQuery(AbstractOperation queryOperation, Map<String, Object> params) {
        if (params != null) {
            params.forEach(queryOperation::setParam);
        }
        queryOperation.execute().ifSuccess(s -> {
            entities = s.asList();
            Stream<T> stream = entities.stream();

            String key = keyword.getValue();
            if (!key.isBlank())
                stream = stream.filter(e -> new KeyWordSearcher<>(e).match(key));

            if (!afterQueryPredicates.isEmpty())
                stream = stream.filter(e -> afterQueryPredicates.values().stream().anyMatch(p -> p.test(e)));

            entities = stream.collect(Collectors.toList());
            pagingList.update(this.entities);
            doRefreshAfterFinishedQuery();
        }).ifException(this::handleException);
        return this;
    }

    private void doRefreshAfterFinishedQuery() {
        setGridItems(entities);
        this.pagingList.update(entities, singlePageSize);
        this.pageBar.setOnePageSize(singlePageSize).setTotalDataSizeFormatter(totalDataSizeFormatter).build();
        this.reloadedListeners.forEach(a -> a.accept(entities));
    }

    private void setGridItems(List<T> entities) {
        if (grid instanceof TreeGrid && subEntitiesProvider != null) {
            TreeData<T> treeData = new TreeData<>();
            treeData.addItems(entities, subEntitiesProvider);
            TreeDataProvider<T> provider = new TreeDataProvider<>(treeData);
            this.grid.setDataProvider(provider);
        } else {
            this.grid.setItems(entities);
        }
    }

    public CrudGridView<T> insertEntity(T entity) {
        entities.add(0, entity);
        doRefreshAfterFinishedQuery();
        return this;
    }

    public CrudGridView<T> updateEntity(T entity) {
        return this.updateEntity(entity, entity::equals);
    }

    public CrudGridView<T> deleteEntity(T entity) {
        return this.deleteEntity(entity::equals);
    }

    public CrudGridView<T> deleteEntity(Predicate<T> isSameEntity) {
        entities.removeIf(isSameEntity);
        doRefreshAfterFinishedQuery();
        return this;
    }

    public CrudGridView<T> updateEntity(T entity, Predicate<T> isSameEntity) {
        entities.replaceAll(o -> isSameEntity.test(o) ? entity : o);
        doRefreshAfterFinishedQuery();
        return this;
    }

    public CrudGridView<T> setSinglePageSize(int singlePageSize) {
        this.singlePageSize = singlePageSize;
        pagingList.setSinglePageSize(singlePageSize);
        pageBar.build();
        return this;
    }

    private void handleException(Exception exception) {
        if (exception instanceof java.net.ConnectException) {
            new ExceptionDialog(exception).setTitle("网络连接异常").open();
        }
//        if (exception instanceof com.mysql.cj.jdbc.exceptions.CommunicationsException) {
//            new ExceptionDialog(exception).setTitle("网络连接异常").open();
//        }

        VerticalLayout layout = new VerticalLayout();
        layout.getStyle().set("color", "red");

        HorizontalLayout toolbar = new HorizontalLayout();
        H4 title = new H4("发生错误");
        Button back = new Button("返回", VaadinIcon.ARROW_BACKWARD.create());
        back.addClickListener(e -> setContent(grid));
        toolbar.setAlignSelf(Alignment.START, title);
        toolbar.setAlignSelf(Alignment.END, back);

        toolbar.add(toolbar);

        layout.add(new HorizontalRule());

        layout.add(new H5(exception.getLocalizedMessage()));

        StringWriter messageWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(messageWriter));
        layout.add(new TextArea(messageWriter.toString()));

        setContent(layout);
    }

    public CrudGridView<T> buildMenuFromManager(Class<? extends IGridMenuManager> manager) {
        return buildMenu(GridMenuBuilder.createContextMenuManager(this, manager));
    }

    public CrudGridView<T> buildMenu(BiConsumer<GridContextMenu<T>, T> consumer) {
        GridContextMenu<T> menu = grid.addContextMenu();
        menu.setDynamicContentHandler(entity -> {
            if (entity == null) {
                return false;
            }
            menu.removeAll();
            grid.select(entity);
            // many for menu.addItems("names", clickListeners)
            consumer.accept(menu, entity);
            return true;
        });

        /*above code can let click listeners working.*/
        /* below code can add item to client page,
         * but can not let click listeners working.
         * (no any response after clicked menu items)
         */
/*

        menu.addGridContextMenuOpenedListener(event -> {
            if (event.isFromClient() && event.isOpened()) {
                event.getSource().removeAll();
                event.getSource().getStyle().set("font-size", "12px");
                event.getItem().ifPresent(t -> {
                    grid.select(t);
                    // many for menu.addItems("names", clickListeners)
                    consumer.accept(event.getSource(), t);
                });
            }
        }).remove();
*/

        return this;
    }

    public CrudGridView<T> buildMenu(Consumer<GridContextMenu<T>> consumer) {
        consumer.accept(grid.addContextMenu());
        return this;
    }

    public CrudGridView<T> itemDoubleClickListener(ComponentEventListener<ItemDoubleClickEvent<T>> listener) {
        grid.addItemDoubleClickListener(listener);
        return this;
    }

    public CrudGridView<T> itemDoubleClicked(Consumer<T> consumer) {
        grid.addItemDoubleClickListener(e -> Optional.ofNullable(e.getItem()).ifPresent(consumer));
        return this;
    }

    public Grid<T> getGrid() {
        return grid;
    }

    public CrudGridView<T> setGrid(Grid<T> grid) {
        grid.setSizeFull();
        this.grid = grid;
        setContent(grid);
        getFooterBarMiddle().removeAll();
        doRefreshAfterFinishedQuery();
        addFooterBarMiddle(pageBar.addDataConsumer(this::setGridItems).build());
        return this;
    }

    public CrudGridView<T> adjustGridStyle() {
        return adjustGridStyle(grid);
    }

    public CrudGridView<T> adjustGridStyle(Grid<T> grid) {
        grid.getColumns().forEach(c -> c.setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true));
        grid.setSizeFull();
        grid.setMultiSort(false);
        return this;
    }

    public CrudGridView<T> addToolBarRightBeforeKeyword(Component... components) {
        HorizontalLayout t = getToolBarRight();
        for (Component c : components) {
            t.addComponentAtIndex(Math.max(0, t.getChildren().collect(Collectors.toList()).indexOf(keyword) - 1), c);
        }
        return this;
    }

    public CrudGridView<T> addToolBarRightBeforeSearch(Component... components) {
        HorizontalLayout t = getToolBarRight();
        for (Component c : components) {
            t.addComponentAtIndex(Math.max(0, t.getChildren().collect(Collectors.toList()).indexOf(search) - 1), c);
        }
        return this;
    }

    public CrudGridView<T> removeKeyword() {
        getToolBarRight().remove(keyword);
        return this;
    }

    public CrudGridView<T> removeSearch() {
        getToolBarRight().remove(search);
        return this;
    }

    public CrudGridView<T> addKeyword() {
        getToolBarRight().add(keyword);
        return this;
    }

    public CrudGridView<T> addSearch() {
        getToolBarRight().add(search);
        return this;
    }

    public CrudGridView<T> reloadedListener(Consumer<List<T>> onReloadListeners) {
        this.reloadedListeners.add(onReloadListeners);
        return this;
    }

    public CrudGridView<T> addQueryParameter(String name, Supplier<Object> supplier) {
        this.parameterMap.put(name, supplier);
        return this;
    }

    public CrudGridView<T> addAfterQueryPredicate(String name, Predicate<T> predicate) {
        if (predicate == null) {
            this.afterQueryPredicates.remove(name);
        } else {
            this.afterQueryPredicates.put(name, predicate);
        }
        return this;
    }

    private ComboBox<GridVariant> createThemeSwitchBox() {
        return new ComboBoxFactory<GridVariant>()
                .lumoSmall()
                .placeholder("默认表格样式")
                .items(Arrays.asList(GridVariant.values()))
                .valueChangeListener(e -> {
                    grid.removeThemeVariants(GridVariant.values());
                    grid.addThemeVariants(e.getValue());
                })
                .clearButtonVisible(true)
                .get();
    }

    public CrudGridView<T> subItemsProvider(ValueProvider<T, Collection<T>> subItemsProvider) {
        this.subEntitiesProvider = subItemsProvider;
        return this;
    }

    public List<T> getEntities() {
        return entities;
    }
}
