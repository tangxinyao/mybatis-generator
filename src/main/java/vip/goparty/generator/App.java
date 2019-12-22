package vip.goparty.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.NullProgressCallback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {
        List<String> warnings = new ArrayList<>();
        File configFile = ResourceUtil.getResourceFile("generatorConfig.xml");
        ConfigurationParser parser = new ConfigurationParser(warnings);
        Configuration config = parser.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(new CustomProgressCallback(warnings));
    }

    public static class CustomProgressCallback extends NullProgressCallback {
        private List<String> warnings;

        CustomProgressCallback(List<String> warnings) {
            this.warnings = warnings;
        }

        @Override
        public void done() {
            if (warnings.isEmpty()) {
                return;
            }
            this.warnings.forEach(System.out::println);
        }
    }

    public static class ResourceUtil {
        static File getResourceFile(final String name) {
            ClassLoader classLoader = ResourceUtil.class.getClassLoader();
            URL url = classLoader.getResource(name);
            if (url == null) {
                throw new RuntimeException(name + " is invalid resource name");
            }
            return new File(url.getPath());
        }
    }
}
