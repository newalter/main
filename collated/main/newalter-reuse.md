# newalter-reuse
###### \java\seedu\address\ui\AutoCompleteTextField.java
``` java
        splitWords();
        SortedSet<String> matchedWords = heuristic.getSuggestions(prefixWords, lastWord);
        if (matchedWords.size() <= 0) {
            dropDownMenu.hide();
            return;
        }

        fillDropDown(matchedWords);
        if (!dropDownMenu.isShowing()) {
            dropDownMenu.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
        }
```
###### \java\seedu\address\ui\AutoCompleteTextField.java
``` java
        List<MenuItem> menuItems = dropDownMenu.getItems();
        menuItems.clear();

        Iterator<String> iterator = matchedWords.iterator();
        int numEntries = Math.min(matchedWords.size(), MAX_ENTRIES);
        for (int i = 0; i < numEntries; i++) {
            menuItems.add(generateMenuItem(iterator.next()));
        }
```
###### \java\seedu\address\ui\AutoCompleteTextField.java
``` java
        final String suggestion = prefixWords + matchedWord;
        MenuItem item = new CustomMenuItem(new Label(suggestion), true);
        // Complete the word with the chosen suggestion when Enter is pressed.
        item.setOnAction((unused) -> complete(item));
        return item;
```
