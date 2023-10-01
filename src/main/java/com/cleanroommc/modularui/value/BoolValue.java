package com.cleanroommc.modularui.value;

import com.cleanroommc.modularui.api.value.IBoolValue;
import com.cleanroommc.modularui.api.value.IIntValue;
import com.cleanroommc.modularui.api.value.IStringValue;
import com.cleanroommc.modularui.utils.BooleanConsumer;

import java.util.function.BooleanSupplier;

public class BoolValue implements IBoolValue<Boolean>, IIntValue<Boolean>, IStringValue<Boolean> {

    private boolean value;

    public BoolValue(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return getBoolValue();
    }

    @Override
    public void setValue(Boolean value, boolean setSource) {
        setBoolValue(value, setSource);
    }

    @Override
    public boolean getBoolValue() {
        return this.value;
    }

    @Override
    public void setBoolValue(boolean val, boolean setSource) {
        this.value = val;
    }

    @Override
    public String getStringValue() {
        return String.valueOf(this.value);
    }

    @Override
    public void setStringValue(String val, boolean setSource) {
        setBoolValue(Boolean.parseBoolean(val), setSource);
    }

    @Override
    public int getIntValue() {
        return this.value ? 1 : 0;
    }

    @Override
    public void setIntValue(int val, boolean setSource) {
        setBoolValue(val == 1, setSource);
    }

    public static class Dynamic implements IBoolValue<Boolean>, IIntValue<Boolean>, IStringValue<Boolean> {

        private final BooleanSupplier getter;
        private final BooleanConsumer setter;

        public Dynamic(BooleanSupplier getter, BooleanConsumer setter) {
            this.getter = getter;
            this.setter = setter;
        }

        @Override
        public boolean getBoolValue() {
            return this.getter.getAsBoolean();
        }

        @Override
        public void setBoolValue(boolean val, boolean setSource) {
            this.setter.accept(val);
        }

        @Override
        public String getStringValue() {
            return String.valueOf(getBoolValue());
        }

        @Override
        public void setStringValue(String val, boolean setSource) {
            setBoolValue(Boolean.parseBoolean(val), setSource);
        }

        @Override
        public Boolean getValue() {
            return getBoolValue();
        }

        @Override
        public void setValue(Boolean value, boolean setSource) {
            setBoolValue(value, setSource);
        }

        @Override
        public int getIntValue() {
            return getBoolValue() ? 1 : 0;
        }

        @Override
        public void setIntValue(int val, boolean setSource) {
            setBoolValue(val == 1, setSource);
        }
    }
}
