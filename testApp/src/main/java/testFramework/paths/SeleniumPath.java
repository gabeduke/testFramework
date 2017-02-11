package testFramework.paths;

import org.openqa.selenium.By;

public interface SeleniumPath {

    String getCssSelector();

    default By getBy(Object... args) {
        return By.cssSelector(String.format(getCssSelector(), args));
    }

    default String getCssSelector(Object... args) {
        return String.format(getCssSelector(), args);
    }

}
