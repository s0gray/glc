package com.ogray.glc;


import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.ByteArrayInputStream;
import java.util.Vector;


/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route
@PWA(name = "glc",
        shortName = "glc",
        description = "Gravitational Lensing Computing",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@Slf4j
public class MainView extends VerticalLayout implements HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<String>, String>> {
    ComboBox<String> comboBoxSourceType = new ComboBox<>("Source");

    Manager boss = new Manager(null);
    Image image = null;

    TextField sourceSizeField = new TextField("Source size (RE)");

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service The message service. Automatically injected Spring managed bean.
     */
    public MainView(@Autowired GreetService service) {
        initComponents();

        boss.init();
        boss.getSrc().setParameter("type", Persist.getInstance().getSourceType());
        boss.getSrc().setParameter("size", Persist.getInstance().getSourceSize());

        log.info("source type = "+boss.src.par.type);
        boss.render();

        final byte[] jpegData = boss.map.field.getJPG();
        StreamResource resource = new StreamResource("image.jpg", () ->
                new ByteArrayInputStream(jpegData));
        image = new Image(resource, "image");

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Render",
                e -> {
            try {
                float srcSize = Float.parseFloat(sourceSizeField.getValue());
                Persist.getInstance().setSourceSize((int) srcSize);
            } catch(NumberFormatException ex) {}

            UI.getCurrent().getPage().reload();
        });

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button has a more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);
        sourceSizeField.setValue(""+Persist.getInstance().getSourceSize());


        add(image);
        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        HorizontalLayout layout = new HorizontalLayout(comboBoxSourceType, sourceSizeField);
        layout.setDefaultVerticalComponentAlignment(Alignment.END);
        add(layout);


        add(button);
    }

    /*byte[] generateSourceImage() {
        if(this.comboBoxSourceType==null || this.comboBoxSourceType.getValue()==null) {
            source = new GausSource(sourceSize, QSize);
        } else
            switch(  this.comboBoxSourceType.getValue()) {
                case FlatSource.NAME:
                    source = new FlatSource(sourceSize, QSize);
                    break;
                case GausSource.NAME:
                    source = new GausSource(sourceSize, QSize);
                    break;
                default:
                    source = new FlatSource(sourceSize, QSize);

            }
       // source.generate();

        byte[][] raw = source.getData();

        int[][] rgb = Utils.makeGreyRGB(raw, sourceSize, sourceSize);
        byte[] jpg = null;
        try {
            jpg = Utils.rawToJpeg(rgb, sourceSize, sourceSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jpg;
    }*/

    void initComponents() {
        String[] sourceTypes = new String[5];
        sourceTypes[0] = "Flat";
        sourceTypes[1] = "Gaussian";
        sourceTypes[2] = "Exponent";
        sourceTypes[3] = "Limb";
        sourceTypes[4] = "A-Disk";
        comboBoxSourceType.setItems(sourceTypes);
        comboBoxSourceType.setValue(sourceTypes[Persist.getInstance().getSourceType()]);
        comboBoxSourceType.addValueChangeListener(this);
    }

    @Override
    public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxStringComponentValueChangeEvent) {
        log.info("valueChanged " + this.comboBoxSourceType.getValue());
        switch (comboBoxSourceType.getValue()) {
            case "Flat":
                Persist.getInstance().setSourceType(0);
                break;
            case "Gaussian":
                Persist.getInstance().setSourceType(1);
                break;
            case "Exponent":
                Persist.getInstance().setSourceType(2);
                break;
            case "Limb":
                Persist.getInstance().setSourceType(3);
                break;
            case "A-Disk":
                Persist.getInstance().setSourceType(4);
                break;
        }
    }
}
