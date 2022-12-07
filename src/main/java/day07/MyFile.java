package day07;

public class MyFile {
    private final String name;

    private final long size;

    public MyFile(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "MyFile{" +
                "name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
