package pl.holidayhouse.house;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.holidayhouse.view.AppLayoutBasic;

@PageTitle("Domki | Summer Holidays")
@Route(value = "/domki", layout = AppLayoutBasic.class)
public class HouseListView extends VerticalLayout {
    private HouseForm form;
    Grid<House> grid = new Grid<>(House.class);
    TextField filterText = new TextField();
    HouseService houseService;

    public HouseListView(HouseService houseService) {
        this.houseService = houseService;
        addClassName("houses-list-view");
        setSizeFull();

        configureGrid();
        configureForm();

        add(
            getToolbar(),
            getContent()
        );
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setHouse(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new HouseForm();
        form.setWidth("25em");
        form.addListener(HouseForm.SaveEvent.class, this::saveHouse);
        form.addListener(HouseForm.DeleteEvent.class, this::deleteHouse);
        form.addListener(HouseForm.CloseEvent.class, e -> closeEditor());
    }

    private void deleteHouse(HouseForm.DeleteEvent event) {
        houseService.delete(event.getHouse());
        updateList();
        closeEditor();
    }

    private void saveHouse(HouseForm.SaveEvent event){
        houseService.save(event.getHouse());
        updateList();
        closeEditor();
    }
    private Component getToolbar() {
        filterText.setPlaceholder("Wyszukaj");
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button addHouseButton = new Button("Dodaj domek", new Icon(VaadinIcon.PLUS));
        addHouseButton.addClickListener(e -> addHouse());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addHouseButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addHouse() {
        grid.asSingleSelect().clear();
        editHouse(new House());
    }

    private void updateList() {
        grid.setItems(houseService.findAll(filterText.getValue()));
    }
    private void configureGrid() {
        grid.addClassName("house-grid");
        grid.setSizeFull();

        grid.setColumns("house_id", "status", "comment");
        grid.getColumnByKey("house_id").setHeader("Numer");
        grid.getColumnByKey("status").setHeader("Status");
        grid.getColumnByKey("comment").setHeader("Komentarz");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editHouse(e.getValue()));
    }

    private void editHouse(House house) {
        if(house == null){
            closeEditor();
        } else {
            form.setHouse(house);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}