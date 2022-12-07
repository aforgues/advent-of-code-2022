package day07;

import java.util.ArrayList;
import java.util.List;

public class MyDirectory {
    private final String name;

    private List<MyFile> files;
    private List<MyDirectory> subDirectories;
    private final MyDirectory parentDirectory;
    public MyDirectory(String name, MyDirectory parentDirectory) {
        this.name = name;
        this.files = new ArrayList<>();
        this.subDirectories = new ArrayList<>();
        this.parentDirectory = parentDirectory;
    }

    public List<MyDirectory> getSubDirectories() {
        return this.subDirectories;
    }

    public MyDirectory getParentDirectory() {
        return this.parentDirectory;
    }

    public void addFile(String name, long size) {
        this.files.add(new MyFile(name, size));
    }

    public void addSubDirectory(MyDirectory subDirectory) {
        this.subDirectories.add(subDirectory);
    }

    public MyDirectory findSubDirectoryByName(String name) {
        return this.subDirectories.stream().filter(directory -> directory.name.equals(name)).findFirst().get();
    }

    public long getTotalSize() {
        long filesSize = this.files.stream().map(MyFile::getSize).reduce(0L, Long::sum);
        long subDirectoriesSize = this.subDirectories.stream().map(MyDirectory::getTotalSize).reduce(0L, Long::sum);
        return filesSize + subDirectoriesSize;
    }

    @Override
    public String toString() {
        return "MyDirectory{" +
                "name='" + name + '\'' +
                ", files=" + files +
                ", subDirectories=" + subDirectories +
                ", parentDirectory=" + (parentDirectory == null ? "ROOT" : parentDirectory.name) +
                '}';
    }
}
