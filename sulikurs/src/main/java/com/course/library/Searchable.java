package com.course.library;

/**
 * Поиск по каталогу — демонстрация интерфейса (полиморфизм по контракту).
 */
public interface Searchable {

    /**
     * @param keyword фрагмент названия или иного поля (без учёта регистра)
     */
    boolean matches(String keyword);
}
