package com.notayessir.queue;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Arrays;
import java.util.Collections;

public class CodeGenerator {



    public static void main(String[] args) {
        String database = "jdbc:mysql://localhost:3306/ez_trade_queue?useUnicode=true&useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true";
        String name = "root";
        String pass = "123456";
        String moduleDirectory = "/Users/geek/IdeaProjects/mercury-ez-trade/ez-trade-queue/src/main/java";
        String xmlDirectory = "/Users/geek/IdeaProjects/mercury-ez-trade/ez-trade-queue/src/main/resources/xml/generate";


        FastAutoGenerator.create(database, name, pass)
                .globalConfig(builder -> {
                    builder.disableOpenDir()
                            .author("nobody")
                            .outputDir(moduleDirectory); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.notayessir.queue.route")// 设置父包名
                            .moduleName("")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, xmlDirectory))
                    ; // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(Arrays.asList("t_match_config")) // 设置需要生成的表名
                            .addTablePrefix("t_")
                            .entityBuilder()
                            .enableLombok()
                            .enableFileOverride()
                    ;
                    // 设置过滤表前缀
                })
                .templateConfig(builder -> {
                    builder.disable(TemplateType.CONTROLLER)
//                            .disable(TemplateType.SERVICE)
//                            .disable(TemplateType.SERVICE_IMPL)
                    ;
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
