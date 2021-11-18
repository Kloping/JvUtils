package io.github.kloping.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 根据JSON生成Java类对象的工具
 */
public class JsonSJC {

    /**
     * 解析一个json对象
     *
     * @param jsonObject json对象
     * @param path       解析后生成的对象储存路劲
     * @param name       主类名称 例如:Main 生成 (Main.java)
     * @param packageN   包名
     * @return 一堆字符
     */
    public static synchronized String parse(JSONObject jsonObject, String path, String name, String packageN) {
        Set<Map.Entry<String, Object>> set = jsonObject.entrySet();
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageN).append(";\n\n");
        sb.append("public class ").append(name).append(" {\n");
        Map<String, String> name2type = new ConcurrentHashMap<>();
        for (Map.Entry<String, Object> en : set) {
            try {
                String key = en.getKey();
                Object v = en.getValue();
                if (v.toString().equalsIgnoreCase("true") || v.toString().equalsIgnoreCase("false")) {
                    name2type.put(key, "Boolean");
                    sb.append("\tprivate Boolean ").append(key).append(";\n");
                } else if (v instanceof String) {
                    name2type.put(key, "String");
                    sb.append("\tprivate String ").append(key).append(";\n");
                } else if (v instanceof Number) {
                    name2type.put(key, "Number");
                    sb.append("\tprivate Number ").append(key).append(";\n");
                } else if (v instanceof JSONObject) {
                    String type = parse((JSONObject) v, path, getFirstBigW(key), packageN);
                    name2type.put(key, type);
                    sb.append("\tprivate ").append(type).append(" ").append(key).append(";\n");
                } else if (v instanceof JSONArray) {
                    Object jo1 = ((JSONArray) v).get(0);
                    v = jo1.toString();
                    if (jo1 instanceof JSONObject) {
                        String type = parse((JSONObject) jo1, path, getFirstBigW(key), packageN) + "[]";
                        name2type.put(key, type);
                        sb.append("\tprivate ").append(type).append(" ").append(key).append(";\n");
                    } else SummonArrayType(sb, name2type, key, v, jo1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        SummonGetterAndSetter(name, sb, name2type);
        putStringInFile(sb.toString(), path, name + ".java");
        return name;
    }

    /**
     * 解析一个json对象 且 不带包名
     *
     * @param name       主类名称 例如:Main 生成 (Main.java)
     * @param show       是否显示错误
     * @param withStatic 是否需要静态类
     * @return 一堆字符
     */
    public static synchronized String parseNoPackage(JSONObject jsonObject, String name, boolean show, boolean withStatic) {
        Set<Map.Entry<String, Object>> set = jsonObject.entrySet();
        StringBuilder sb = new StringBuilder();
        sb.append("public");
        if (withStatic) sb.append(" static");
        sb.append(" class ").append(name).append(" {\n");
        Map<String, String> name2type = new ConcurrentHashMap<>();
        for (Map.Entry<String, Object> en : set) {
            try {
                String key = en.getKey();
                Object v = en.getValue();
                if (v.toString().equalsIgnoreCase("true") || v.toString().equalsIgnoreCase("false")) {
                    name2type.put(key, "Boolean");
                    sb.append("\tprivate Boolean ").append(key).append(";\n");
                } else if (v instanceof String) {
                    name2type.put(key, "String");
                    sb.append("\tprivate String ").append(key).append(";\n");
                } else if (v instanceof Number) {
                    name2type.put(key, "Number");
                    sb.append("\tprivate Number ").append(key).append(";\n");
                } else if (v instanceof JSONObject) {
                    String type = parseNoPackage((JSONObject) v, getFirstBigW(key), show, withStatic);
                    name2type.put(key, type);
                    sb.append("\tprivate ").append(type).append(" ").append(key).append(";\n");
                } else if (v instanceof JSONArray) {
                    Object jo1 = ((JSONArray) v).get(0);
                    v = jo1.toString();
                    if (jo1 instanceof JSONObject) {
                        String type = parseNoPackage((JSONObject) jo1, getFirstBigW(key), show, withStatic) + "[]";
                        name2type.put(key, type);
                        sb.append("\tprivate ").append(type).append(" ").append(key).append(";\n");
                    } else {
                        SummonArrayType(sb, name2type, key, v, jo1);
                    }
                }
            } catch (Exception e) {
                if (show)
                    e.printStackTrace();
                continue;
            }
        }
        SummonGetterAndSetter(name, sb, name2type);
        return name;
    }

    /**
     * 解析一个json对象
     *
     * @param jsonObject json对象
     * @param path       解析后生成的对象储存路劲
     * @param name       主类名称 例如:Main 生成 (Main.java)
     * @param packageN   包名
     * @param su1        是否生成Getter Setter
     * @return 一堆字符
     */
    public static synchronized String parse(JSONObject jsonObject, String path, String name, String packageN, boolean su1) {
        Set<Map.Entry<String, Object>> set = jsonObject.entrySet();
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageN).append(";\n\n");
        sb.append("public class ").append(name).append(" {\n");
        Map<String, String> name2type = new ConcurrentHashMap<>();
        for (Map.Entry<String, Object> en : set) {
            try {
                String key = en.getKey();
                Object v = en.getValue();
                if (v.toString().equalsIgnoreCase("true") || v.toString().equalsIgnoreCase("false")) {
                    name2type.put(key, "Boolean");
                    sb.append("\tprivate Boolean ").append(key).append(";\n");
                } else if (v instanceof String) {
                    name2type.put(key, "String");
                    sb.append("\tprivate String ").append(key).append(";\n");
                } else if (v instanceof Number) {
                    name2type.put(key, "Number");
                    sb.append("\tprivate Number ").append(key).append(";\n");
                } else if (v instanceof JSONObject) {
                    String type = parse((JSONObject) v, path, getFirstBigW(key), packageN, su1);
                    name2type.put(key, type);
                    sb.append("\tprivate ").append(type).append(" ").append(key).append(";\n");
                } else if (v instanceof JSONArray) {
                    Object jo1 = ((JSONArray) v).get(0);
                    v = jo1.toString();
                    if (jo1 instanceof JSONObject) {
                        String type = parse((JSONObject) jo1, path, getFirstBigW(key), packageN, su1) + "[]";
                        name2type.put(key, type);
                        sb.append("\tprivate ").append(type).append(" ").append(key).append(";\n");
                    } else SummonArrayType(sb, name2type, key, v, jo1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        if (su1)
            SummonGetterAndSetter(name, sb, name2type);
        else sb.append("}");

        putStringInFile(sb.toString(), path, name + ".java");
        return name;
    }

    /**
     * 解析一个json对象 且 不带包名
     *
     * @param jsonObject JSON对象
     * @param name       主类名称 例如:Main 生成 (Main.java)
     * @param show       是否显示错误
     * @param withStatic 是否需要静态类
     * @param su         是否生成Getter Setter
     * @return 一堆字符
     */
    public static synchronized String parseNoPackage(JSONObject jsonObject, String name, boolean show, boolean withStatic, boolean su) {
        Set<Map.Entry<String, Object>> set = jsonObject.entrySet();
        StringBuilder sb = new StringBuilder();
        sb.append("public");
        if (withStatic) sb.append(" static");
        sb.append(" class ").append(name).append(" {\n");
        Map<String, String> name2type = new ConcurrentHashMap<>();
        for (Map.Entry<String, Object> en : set) {
            try {
                String key = en.getKey();
                Object v = en.getValue();
                if (v.toString().equalsIgnoreCase("true") || v.toString().equalsIgnoreCase("false")) {
                    name2type.put(key, "Boolean");
                    sb.append("\tprivate Boolean ").append(key).append(";\n");
                } else if (v instanceof String) {
                    name2type.put(key, "String");
                    sb.append("\tprivate String ").append(key).append(";\n");
                } else if (v instanceof Number) {
                    name2type.put(key, "Number");
                    sb.append("\tprivate Number ").append(key).append(";\n");
                } else if (v instanceof JSONObject) {
                    String type = parseNoPackage((JSONObject) v, getFirstBigW(key), show, withStatic, su);
                    name2type.put(key, type);
                    sb.append("\tprivate ").append(type).append(" ").append(key).append(";\n");
                } else if (v instanceof JSONArray) {
                    Object jo1 = ((JSONArray) v).get(0);
                    v = jo1.toString();
                    if (jo1 instanceof JSONObject) {
                        String type = parseNoPackage((JSONObject) jo1, getFirstBigW(key), show, withStatic, su) + "[]";
                        name2type.put(key, type);
                        sb.append("\tprivate ").append(type).append(" ").append(key).append(";\n");
                    } else {
                        SummonArrayType(sb, name2type, key, v, jo1);
                    }
                }
            } catch (Exception e) {
                if (show)
                    e.printStackTrace();
                continue;
            }
        }
        if (su)
            SummonGetterAndSetter(name, sb, name2type);
        else sb.append("}");
        return name;
    }

    private static void SummonGetterAndSetter(String name, StringBuilder sb, Map<String, String> name2type) {
        for (String key : name2type.keySet()) {
            sb.append("\n");
            String keyN = getFirstBigW(key);
            sb.append("\tpublic ").append(name2type.get(key)).append(" get").append(keyN).append("(){\n");
            sb.append("\t\treturn ").append("this.").append(key).append(";").append("\n\t}");
            sb.append("\n\n\tpublic ").append(name).append(" set").append(keyN).append("(").append(name2type.get(key)).append(" ").append(key)
                    .append(") ").append("{\n");
            sb.append("\t\t").append("this.").append(key).append(" = ").append(key).append(";").append("\n\t\treturn this;").append("\n\t}\n");
        }
        sb.append("}");
    }

    private static void SummonArrayType(StringBuilder sb, Map<String, String> name2type, String key, Object v, Object jo1) {
        if (jo1 instanceof String) {
            name2type.put(key, "String[]");
            sb.append("\tprivate String[] ").append(key).append(";\n");
        } else if (jo1 instanceof Number) {
            name2type.put(key, "Number[]");
            sb.append("\tprivate Number[] ").append(key).append(";\n");
        } else if (v.toString().equalsIgnoreCase("true") || v.toString().equalsIgnoreCase("false")) {
            name2type.put(key, "Boolean[]");
            sb.append("\tprivate Boolean[] ").append(key).append(";\n");
        }
    }

    private static String getFirstBigW(String m1) {
        String m2 = m1.substring(0, 1).toUpperCase();
        return m2 + m1.substring(1);
    }

    private static void putStringInFile(String str, String path, String name) {
        try {
            File file = new File(path, name);
            file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(str.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
