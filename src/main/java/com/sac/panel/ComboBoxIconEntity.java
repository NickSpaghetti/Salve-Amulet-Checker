package com.sac.panel;

import javax.annotation.Nullable;
import javax.swing.Icon;
import lombok.AllArgsConstructor;
import lombok.Getter;

    @AllArgsConstructor
    @Getter
    public class ComboBoxIconEntity<T>
    {
        private Icon icon;
        private String text;
        @Nullable
        private T data;
    }

