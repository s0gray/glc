package com.ogray.glc;

import com.ogray.glc.source.FlatSource;
import com.ogray.glc.source.GausSource;
import com.ogray.glc.source.Source;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
    Source source = null;
    final int sourceSize = 400;
    final int QSize = 100;

    Image image = null;
    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service The message service. Automatically injected Spring managed bean.
     */
    public MainView(@Autowired GreetService service) {
        final byte[] jpegData = generateSourceImage();
        StreamResource resource = new StreamResource("image.jpg", () ->
                new ByteArrayInputStream(jpegData));
        image = new Image(resource, "image");
        add(image);

     //   TextField textField = new TextField("Source type");
     //   textField.addThemeName("bordered");
        // Button click listeners can be defined as lambda expressions
  /*      Button button = new Button("Say hello",
                e -> Notification.show(service.greet(textField.getValue())));

                        // Theme variants give you predefined extra styles for components.
        // Example: Primary button has a more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

 */
        String []sources = new String[2];
        sources[0] = FlatSource.NAME;
        sources[1] = GausSource.NAME;
        comboBoxSourceType.setItems(sources);
        comboBoxSourceType.setValue("Gaussian");
      //  comboBoxSourceType.setItemLabelGenerator(Country::getName);
        comboBoxSourceType.addValueChangeListener(this);

        add(comboBoxSourceType);


        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        add(comboBoxSourceType);
    }

    byte[] generateSourceImage() {
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
        source.generate();

        byte[][] raw = source.getData();

        int[][] rgb = Utils.makeGreyRGB(raw, sourceSize, sourceSize);
        byte[] jpg = null;
        try {
            jpg = Utils.rawToJpeg(rgb, sourceSize, sourceSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jpg;
    }

    @Override
    public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxStringComponentValueChangeEvent) {
        log.info("valueChanged " + this.comboBoxSourceType.getValue());
        final byte[] jpegData = generateSourceImage();
        StreamResource resource = new StreamResource("image.jpg", () ->
                new ByteArrayInputStream(jpegData));
        this.image = new Image(resource, "image");
        //canvas.requestRepaint();

    }
}
