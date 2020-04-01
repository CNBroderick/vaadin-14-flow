/*
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 * Author: Broderick Johansson
 * E-mail: z@bkLab.org
 * Modify date：2020-04-01 15:45:14
 * _____________________________
 * Project name: vaadin-14-flow
 * Class name：org.bklab.flow.CrudGridView
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.flow;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
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
import com.vaadin.flow.server.VaadinSession;
import dataq.core.jdbc.DBAccess;
import dataq.core.operation.AbstractOperation;
import dataq.core.operation.JdbcQueryOperation;
import org.bklab.element.HasAbstractOperation;
import org.bklab.flow.component.HorizontalPageBar;
import org.bklab.flow.component.HorizontalRule;
import org.bklab.flow.dialog.ExceptionDialog;
import org.bklab.flow.factory.*;
import org.bklab.flow.menu.GridMenuBuilder;
import org.bklab.flow.menu.IGridMenuManager;
import org.bklab.flow.tools.MobileBrowserPredicate;
import org.bklab.util.PagingList;
import org.bklab.util.UrlParameterParser;
import org.bklab.util.search.common.KeyWordSearcher;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("UnusedReturnValue")
public class CrudGridView<T> extends TmbView<CrudGridView<T>> {

    private final List<Consumer<List<T>>> reloadedListeners = new ArrayList<>();
    private final Map<String, Predicate<T>> afterQueryPredicates = new HashMap<>();
    private final Map<String, Supplier<Object>> parameterMap = new HashMap<>();
    private final HorizontalPageBar<T> pageBar;
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
    private Boolean mobileMode = false;

    private final List<BiConsumer<CrudGridView<T>, List<T>>> afterQueryConsumers = new ArrayList<>();

    private Supplier<Class<? extends IGridMenuManager>> gridMenuManagerSupplier = () -> null;

    private ValueProvider<T, Collection<T>> subEntitiesProvider = null;
    private final VerticalLayout conditionLayout = new VerticalLayoutFactory().widthFull().visible(false).get();
    private final Map<String, Component> conditionComponentMap = new LinkedHashMap<>();
    private final Button conditionButton = new ButtonFactory().icon(VaadinIcon.ANGLE_DOUBLE_DOWN)
            .peek(b -> b.getElement().setAttribute("title", "展开搜索条件"))
            .clickListener(e -> {
                conditionLayout.setVisible(!conditionLayout.isVisible());
                e.getSource().setIcon(conditionLayout.isVisible() ? VaadinIcon.ANGLE_DOUBLE_UP.create() : VaadinIcon.ANGLE_DOUBLE_DOWN.create());
                e.getSource().getElement().setAttribute("title", (conditionLayout.isVisible() ? "展开" : "合起") + "搜索条件");
            }).lumoIcon().lumoSmall().get();
    private final Map<String, Integer> conditionColspanMap = new HashMap<>();
    private final Map<String, String> conditionLabelMap = new HashMap<>();
    private BiPredicate<T, T> defaultSameEntityBiPredicate = Objects::equals;
    private final Map<String, Predicate<T>> columnFilterPredicateMap = new HashMap<>();
    private final Map<String, ComboBox<T>> columnFilterComponentMap = new HashMap<>();


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

    public CrudGridView<T> setQueryOperation(HasAbstractOperation operation) {
        this.queryOperation = operation.createAbstractOperation();
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

    public List<T> doLocalQuery() {
        return doLocalQuery(new ArrayList<>());
    }

    @SafeVarargs
    public final List<T> doLocalQuery(Predicate<T>... predicates) {
        return doLocalQuery(Arrays.asList(predicates));
    }

    public List<T> doLocalQuery(List<Predicate<T>> predicates) {
        String key = keyword.getValue();
        Stream<T> stream = entities.stream();
        if (!key.isBlank()) {
            stream = entities.stream().filter(e -> new KeyWordSearcher<>(e).match(key));
        }

        if (!predicates.isEmpty()) {
            stream = entities.stream().filter(e -> predicates.stream().allMatch(p -> p.test(e)));
        }

        if (!columnFilterPredicateMap.isEmpty()) {
            stream = entities.stream().filter(e -> columnFilterPredicateMap.values().stream().allMatch(p -> p.test(e)));
        }
        List<T> collect = stream.collect(Collectors.toList());
        pagingList.update(collect);
        grid.setItems(collect);
        pagingList.update(collect, singlePageSize);
        pageBar.setOnePageSize(singlePageSize).setTotalDataSizeFormatter(totalDataSizeFormatter).build();
        afterQueryConsumers.forEach(c -> c.accept(this, collect));
        return collect;
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

    public CrudGridView<T> resetEntities(Collection<T> entities) {
        this.entities.clear();
        this.entities.addAll(entities);
        return this;
    }

    public CrudGridView<T> minimalPageBar() {
        this.pageBar.minimal();
        return this;
    }

    public void doRefreshAfterFinishedQuery() {
        doRefreshAfterFinishedQuery(entities);
    }

    public void doRefreshAfterFinishedQuery(List<T> entities) {
        this.pagingList.update(entities, singlePageSize);
        this.pageBar.setOnePageSize(singlePageSize).setTotalDataSizeFormatter(totalDataSizeFormatter).build();
        this.reloadedListeners.forEach(a -> a.accept(entities));
        this.afterQueryConsumers.forEach(a -> a.accept(this, entities));
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
        if (exception instanceof com.mysql.cj.jdbc.exceptions.CommunicationsException) {
            new ExceptionDialog(exception).setTitle("数据库连接异常").open();
        }

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

    public CrudGridView<T> buildMenuFromManager() {
        return buildMenuFromManager(gridMenuManagerSupplier.get());
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
        setContent(conditionLayout, grid);
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

    public CrudGridView<T> setGridMenuManagerSupplier(Supplier<Class<? extends IGridMenuManager>> gridMenuManagerSupplier) {
        this.gridMenuManagerSupplier = gridMenuManagerSupplier;
        return this;
    }

    public CrudGridView<T> addAfterQueryConsumer(BiConsumer<CrudGridView<T>, List<T>> afterQueryConsumer) {
        afterQueryConsumers.add(afterQueryConsumer);
        return this;
    }

    public CrudGridView<T> mobileMode() {
        this.mobileMode = true;
        minimalPageBar();
        new TextFieldFactory(keyword).placeholder("关键字").width("6em").maxWidth("40vw");
        search.setWidth("4em");
        return this;
    }

    public CrudGridView<T> mobileMode(boolean mobileMode) {
        this.mobileMode = mobileMode;
        if (mobileMode) mobileMode();
        return this;
    }

    public CrudGridView<T> autoAdaptMobileMode() {
        return mobileMode(new MobileBrowserPredicate().test(VaadinSession.getCurrent()));
    }

    public CrudGridView<T> defaultSameEntityBiPredicate(BiPredicate<T, T> defaultSameEntityBiPredicate) {
        this.defaultSameEntityBiPredicate = defaultSameEntityBiPredicate;
        return this;
    }

    public CrudGridView<T> scrollToEntity(T entity) {
        return scrollToEntity(entity, defaultSameEntityBiPredicate);
    }

    public CrudGridView<T> scrollToEntity(T object, BiPredicate<T, T> defaultSameEntityBiPredicate) {
        T entity = entities.stream().filter(t -> defaultSameEntityBiPredicate.test(object, t)).findFirst().orElse(null);
        if (entity != null) {
            pageBar.switchPage(entity);
            int index = pagingList.indexOfPage(entity);
            if (index >= 0) grid.scrollToIndex(index);
        }
        return this;
    }

    public CrudGridView<T> scrollToEntityFromUrlParameter(String parameter, String paramName, Function<String, T> stringToEntityFunction) {
        Optional.ofNullable(new UrlParameterParser(parameter).getValue(paramName)).map(stringToEntityFunction).ifPresent(entity -> scrollToEntity(entity, defaultSameEntityBiPredicate));
        return this;
    }

    public CrudGridView<T> scrollToEntityFromUrlParameter(String parameter, String paramName, Function<String, T> stringToEntityFunction, BiPredicate<T, T> sameEntityBiPredicate) {
        Optional.ofNullable(new UrlParameterParser(parameter).getValue(paramName)).map(stringToEntityFunction).ifPresent(entity -> scrollToEntity(entity, sameEntityBiPredicate));
        return this;
    }

    public <E extends Component & HasValue<?, ?>> CrudGridView<T> addCondition(String conditionName, E component) {
        return addCondition(conditionName, component, component::getValue, 1);
    }

    public <E extends Component & HasValue<?, ?>> CrudGridView<T> addCondition(String conditionName, E component, int colspan) {
        return addCondition(conditionName, component, component::getValue, colspan);
    }

    public CrudGridView<T> addCondition(String conditionName, Component component, Supplier<Object> valueSupplier) {
        return addCondition(conditionName, component, valueSupplier, 1);
    }

    public CrudGridView<T> addCondition(String conditionName, Component component, Supplier<Object> valueSupplier, int colspan) {
        return addCondition(null, conditionName, component, valueSupplier, colspan);
    }

    public CrudGridView<T> addCondition(String labelName, String parameterName, Component component, Supplier<Object> valueSupplier, int colspan) {
        if (component == null) {
            conditionComponentMap.remove(parameterName);
            return this;
        }
        conditionComponentMap.put(parameterName, component);
        parameterMap.put(parameterName, valueSupplier);
        if (colspan < 1) colspan = 1;
        conditionColspanMap.put(parameterName, colspan);
        if (labelName != null) conditionLabelMap.put(parameterName, labelName);
        return this;
    }

    public CrudGridView<T> addCondition(Consumer<CrudGridView<T>> crudGridViewConsumer) {
        crudGridViewConsumer.accept(this);
        return this;
    }

    public CrudGridView<T> buildConditionLayout() {
        if (conditionComponentMap.isEmpty()) {
            conditionLayout.setVisible(false);
            conditionButton.setVisible(false);
            return this;
        }
        conditionButton.setVisible(true);
        conditionLayout.removeAll();
        addToolBarRight(conditionButton);
        FormLayout formLayout = new FormLayout();
        conditionComponentMap.forEach((name, component) -> {
            if (conditionLabelMap.containsKey(name)) {
                formLayout.addFormItem(component, conditionLabelMap.get(name));
                formLayout.setColspan(component, conditionColspanMap.getOrDefault(name, 1));
            } else {
                formLayout.add(component, conditionColspanMap.getOrDefault(name, 1));
            }

        });
        formLayout.add(new TextFieldFactory().valueChangeListener(e -> this.keyword.setValue(e.getValue()))
                .label("关键字：").widthFull().lumoSmall().get());
        formLayout.setWidthFull();
        conditionLayout.add(formLayout);

        Button search = new ButtonFactory().clickListener(e -> {
            doQuery();
            conditionButton.click();
        }).icon(VaadinIcon.SEARCH).text("查询").minWidth("6em").maxWidth("30vw").get();
        Button close = new ButtonFactory().clickListener(e -> conditionButton.click()).icon(VaadinIcon.CLOSE).text("关闭").minWidth("6em").maxWidth("30vw").get();
        conditionLayout.add(new HorizontalLayoutFactory().add(search, close).get());
        conditionLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        return this;
    }

    public CrudGridView<T> setConditionLayout(Component... components) {
        conditionLayout.removeAll();
        conditionLayout.add(components);
        return this;
    }

    public CrudGridView<T> addColumnFilter(String columnKey, String columnName, Function<T, String> valueProvider) {
        return addColumnFilter(columnKey, columnName, valueProvider, (s, t) -> s == null
                || valueProvider.apply(s).equals(valueProvider.apply(t)));
    }


    public CrudGridView<T> addColumnFilter(String columnKey, String columnName, Function<T, String> valueProvider, BiPredicate<T, T> predicate) {
        Grid.Column<T> column = grid.getColumnByKey(columnKey);
        if (column == null) return this;
        ComboBox<T> comboBox = new ComboBoxFactory<T>().lumoSmall().widthFull().placeholder("商品名称")
                .itemLabelGenerator(valueProvider::apply)
                .allowCustomValue(true).clearButtonVisible(true).valueChangeListener(e -> {
                    List<T> list = doLocalQuery();
                    for (ComboBox<T> c : columnFilterComponentMap.values()) {
                        if (c != e.getSource()) {
                            ItemLabelGenerator<T> generator = c.getItemLabelGenerator();
                            c.setItems(list.stream().collect(Collectors.toMap(generator, Function.identity())).values());
                        }
                    }
                }).get();
        reloadedListeners.add(objects ->
                comboBox.setItems(objects.stream().collect(Collectors.toMap(valueProvider, Function.identity())).values())
        );
        column.setHeader(comboBox);
        columnFilterPredicateMap.put(columnKey, t -> comboBox.getValue() == null || predicate.test(comboBox.getValue(), t));
        columnFilterComponentMap.put(columnKey, comboBox);
        return this;
    }

    public CrudGridView<T> removeColumnFilter(String columnKey, String columnName) {
        columnFilterPredicateMap.remove(columnKey);
        Grid.Column<T> column = grid.getColumnByKey(columnKey);
        if (column == null) return this;
        column.setHeader(columnName);
        return this;
    }

}
