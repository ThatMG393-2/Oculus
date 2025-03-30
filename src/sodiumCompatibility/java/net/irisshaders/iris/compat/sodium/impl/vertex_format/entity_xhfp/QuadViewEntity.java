package net.irisshaders.iris.compat.sodium.impl.vertex_format.entity_xhfp;

import net.irisshaders.iris.vertices.views.QuadView;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Pointer;

import java.nio.ByteBuffer;

public abstract class QuadViewEntity implements QuadView {
  long writePointer;
  int stride;

  @Override
  public float x(int index) {
    return getFloat(index, 0);
  }

  @Override
  public float y(int index) {
    return getFloat(index, 4);
  }

  @Override
  public float z(int index) {
    return getFloat(index, 8);
  }

  @Override
  public float u(int index) {
    return getFloat(index, 16);
  }

  @Override
  public float v(int index) {
    return getFloat(index, 20);
  }

  abstract float getFloat(int index, int offset);

  public static class QuadViewEntityUnsafe extends QuadViewEntity {
    private int stride;
    private long writePointer;

    public void setup(long writePointer, int stride) {
      this.writePointer = writePointer & ~((1L << Pointer.POINTER_SHIFT) - 1);
      this.stride = (stride + (1 << Pointer.POINTER_SHIFT) - 1) &
          ~((1L << Pointer.POINTER_SHIFT) - 1);
    }

    @Override
    float getFloat(int index, int offset) {
      long address = writePointer + ((long) stride * index & 0xFFFFFFFFL) +
          (offset & 0xFFFFFFFFL);
      return Float.intBitsToFloat(MemoryUtil.memGetInt(address));
    }
  }

  public static class QuadViewEntityNio extends QuadViewEntity {
    private ByteBuffer buffer;
    private int stride;
    private long writePointer;

    public void setup(ByteBuffer buffer, int writePointer, int stride) {
      this.buffer = buffer;
      this.writePointer = writePointer;
      this.stride = stride;
    }

    @Override
    float getFloat(int index, int offset) {
      long address = ((long) stride * index & 0xFFFFFFFFL) +
          (offset & 0xFFFFFFFFL);
      return buffer.getFloat((int) address);
    }
  }
}
