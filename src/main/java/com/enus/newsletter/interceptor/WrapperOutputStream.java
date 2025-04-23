package com.enus.newsletter.interceptor;

import java.io.ByteArrayOutputStream;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

class WrapperOutputStream extends ServletOutputStream {
    private final ByteArrayOutputStream buffer;

    public WrapperOutputStream(ByteArrayOutputStream buffer) {
        this.buffer = buffer;
    }

    @Override
    public void write(int b) {
        buffer.write(b);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new UnsupportedOperationException("Not implemented");
    }
}