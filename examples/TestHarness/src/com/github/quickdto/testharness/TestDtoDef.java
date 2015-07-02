package com.github.quickdto.testharness;

import java.util.List;
import java.util.Set;

import com.github.quickdto.shared.EqualsHashCode;
import com.github.quickdto.shared.IgnoreDirty;
import com.github.quickdto.shared.QuickDto;
import com.github.quickdto.shared.ReadOnly;
import com.github.quickdto.testharness.impl.TestImpl;

@QuickDto(source = TestImpl.class)
abstract public class TestDtoDef {
    @EqualsHashCode int id;

    List<BasicTypesDtoDef> children;
    Set<BasicTypesDtoDef> childSet;
    BasicTypesDtoDef child;
    //ChildDto childDto;
    //This will create getter only
    @ReadOnly int readOnly;
    @IgnoreDirty int ignoreDirty;
    @IgnoreDirty BasicTypesDtoDef childIgnoreDirty;

    //This will create both a setter and getter
    abstract void setMethodInt(int methodInt);
    abstract void setMethodBoolean(boolean methodBoolean);
    abstract void setMethodString(String methodString);

    //This will create getter only
    abstract int getMethodReadOnly();

    //This will create both methods
    abstract int getMethodReadWrite();
    abstract void setMethodReadWrite(int methodReadWrite);

    @IgnoreDirty
    abstract void setMethodIgnoreDirty(int methodIgnoreDirty);

    @IgnoreDirty
    abstract void setMethodIgnoreDirty(BasicTypesDtoDef methodIgnoreDirty);

}
