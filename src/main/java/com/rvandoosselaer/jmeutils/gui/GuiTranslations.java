package com.rvandoosselaer.jmeutils.gui;

import com.rvandoosselaer.jmeutils.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A singleton implementation that loads a 'translations/gui' resource bundle to retrieve translated values based on a
 * key. The language can be specified using the 'user.language' property. eg. -Duser.language=en will look for the
 * 'translations/gui_en.properties' file.
 * When a key isn't found in the resource bundle when it's retrieved using the {@link #t(String)} method, the value of
 * the key itself will be returned.
 *
 * @author rvandoosselaer
 */
@Slf4j
public class GuiTranslations {

    private static final String RESOURCE_BUNDLE_NAME = "translations/gui";

    private static GuiTranslations ourInstance = new GuiTranslations();

    private ResourceBundle guiResourceBundle = null;

    private GuiTranslations() {
        log.info("Loading {} translations with locale {}", RESOURCE_BUNDLE_NAME, Locale.getDefault());
        try {
            guiResourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
        } catch (MissingResourceException e) {
            log.error("Resource bundle {} not found!", RESOURCE_BUNDLE_NAME, e);
        }
    }

    public static GuiTranslations getInstance() {
        return ourInstance;
    }

    /**
     * Retrieves the value of the given key from the resource bundle.
     *
     * @param key : key to look for
     * @return the string for the given key in the resource bundle or the key itself when the key isn't found
     */
    public String t(String key) {
        if (guiResourceBundle == null) {
            return key;
        }

        return guiResourceBundle.containsKey(key) ? guiResourceBundle.getString(key) : key;
    }

    /**
     * Retrieves the value of the given key from the resource bundle. Parameters can be supplied, these parameters will
     * be camelcased and inserted in the given key.
     * eg. t("button.{0}.{1}.title", "Hello World", "foo") will look for key: "button.helloWorld.foo.title"
     * in the given key String
     *
     * @param key    the key to look for
     * @param params the params to insert in the key
     * @return the string for the given key
     */
    public String t(String key, Object... params) {
        return t(new MessageFormat(key)
                .format(Arrays.stream(params).map(param -> StringUtils.toCamelCase(String.valueOf(param))).toArray()));
    }

}
