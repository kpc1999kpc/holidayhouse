package pl.holidayhouse.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import pl.holidayhouse.customer.CustomerListView;
import pl.holidayhouse.employee.EmployeeListView;
import pl.holidayhouse.house.HouseListView;
import pl.holidayhouse.payment.PaymentListView;
import pl.holidayhouse.reservation.ReservationListView;
import pl.holidayhouse.salary.SalaryListView;

@Route("")
public class AppLayoutBasic extends AppLayout {

    public AppLayoutBasic() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Holiday House");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        Tabs tabs = getTabs();

        addToDrawer(tabs);
        addToNavbar(toggle, title);
    }
    // end::snippet[]

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(
                createTab(VaadinIcon.CALENDAR, "Rezerwacje", ReservationListView.class),
                createTab(VaadinIcon.MONEY_DEPOSIT, "Płatności", PaymentListView.class),
                createTab(VaadinIcon.USER_HEART, "Klienci", CustomerListView.class),
                createTab(VaadinIcon.USER_STAR, "Pracownicy", EmployeeListView.class),
                createTab(VaadinIcon.MONEY_WITHDRAW, "Wypłaty", SalaryListView.class),
                createTab(VaadinIcon.HOME, "Domki", HouseListView.class)
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Class className) {
        Icon icon = viewIcon.create();
        icon.getStyle()
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setRoute(className);
        link.setTabIndex(-1);

        return new Tab(link);
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName) {
        Icon icon = viewIcon.create();
        icon.getStyle()
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));

        link.setTabIndex(-1);

        return new Tab(link);
    }
}
