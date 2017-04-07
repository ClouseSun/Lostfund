package cn.bit.file;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by zhehua on 07/04/2017.
 */
public class FileMappingUtilsTest {
    @Test
    public void removeMappingFromXml() throws Exception {
        List<String> removed = new ArrayList<>();
        removed.add("hello");

        FileMappingUtils.removeMappingFromXml("src/test/resources/raw/to_be_deleted_filemap", removed, true);
        Assert.assertTrue(!new File("src/test/resources/to_be_deleted_dir").exists());
    }
}