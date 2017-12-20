package com.borderTest;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

/**
 * JSON格式的参数化。
 * 稍微复杂，如果自身的value也是JSON格式，则存在自己调用自己的情况。
 * 由于value的类型并不明确，所以其功能依赖于其它类型的参数校验即检验规则。
 * 所以JSON格式的参数化数量会非常多，但边界测试参数数量多也是在所难免。
 * 用例建议JSON层次不要太高（对于JSONArray至少3层循环），而JSON中的内容可以多一些。
 */
public class JSONBorderParam extends BorderParam {

    JSONBorderParam(String paramPair, String key, String value) {
        // 整个参数的过短，过长，为空还是要判断的。
        super(paramPair, key, value);
    }

    @Override
    public Set<String> getParamList() {
        // 有则替换成解码后的value
        String valueDecode = value.replaceAll("%5B", "[").replaceAll("%7B", "{")
                .replaceAll("%3A", ":").replaceAll("%2C", ",")
                .replaceAll("%7D", "}").replaceAll("%5D", "]")
                .replaceAll("%22", "\\\"").replaceAll("%27", "'");

        if (valueDecode.endsWith("]")) {//这是JSONArray格式，用"]"结尾
            JSONArray jsonArray = JSONArray.fromObject(valueDecode);
            // 遍历其中的JSONObj
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                dealJsonObj(key, jsonObj, jsonArray);
            }
        } else {//普通JSON对象，但是不保证其中还存在JSONArray
            JSONObject jsonObj = JSONObject.fromObject(valueDecode);
            dealJsonObj(key, jsonObj, jsonObj);
        }

        return super.getParamList();
    }

    /**
     * 处理JSON对象的公共方法，将其改变保存到paramPairList中。
     *
     * @param key     最上层的被参数化的key
     * @param jsonObj 要被改变的JsonObj对象
     */
    private void dealJsonObj(String key, JSONObject jsonObj, Object jsonObjReplace) {
        // 每个JSONObj都有很多key，再遍历他。
        Iterator it = jsonObj.keys();
        while (it.hasNext()) {
            // 得到其中的一个key值
            String oneKey = it.next().toString();
            // 保存value值，用于用后需要还原，因为我们需要对每一个key都做处理。
            String valueOriginal = jsonObj.getString(oneKey);

            // 如何replay 是关键。
            BorderParam borderParam = BorderCheckUtil.getBorderParamType(
                    oneKey + "=" + valueOriginal, oneKey, valueOriginal, LONG_PARAM_NO_NEED_CHECK);
            for (String paramPairFix : borderParam.getParamList()) {

                // JSON格式太长的，增加了标红用于前台展示
                // 可能存在递归调用，只为非JSON格式数据增加两边的颜色标识
                String valueFixClear = BorderCheckUtil.getParamValue(paramPairFix);
                if (valueFixClear.contains("}")) {
                    jsonObj.replace(oneKey, valueFixClear);
                } else {
                    jsonObj.replace(oneKey, BorderCheckUtil.HTML_POINT_RED +
                            valueFixClear + BorderCheckUtil.HTML_POINT_SUFFIX);
                }
                // 已经替换关键value，可以直接调用toString方法。
                paramPairList.add(key + "=" + jsonObjReplace);
            }
            // 把数据还原
            jsonObj.replace(oneKey, valueOriginal);
        }
    }
}
