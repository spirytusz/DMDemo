package com.zspirytus.dmdemo.JavaSource.WebServiceUtils;

import android.util.Log;

import com.zspirytus.dmdemo.JavaSource.Utils.XmlUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ZSpirytus on 2017/11/3.
 */

public class WebServiceConnector {

    public static final String METHOD_GETBASICINFOBYSNO = "getBasicInfoBySno";
    public static final String METHOD_GETREPAIRBASICINFOBYSNO = "getRepairBasicInfoBySno ";
    public static final String METHOD_GETRETURNLATELYBASICINFO = "getReturnLatelyBasicInfo";
    public static final String METHOD_GETSLSBASICINFO = "getSLSBasicInfo";
    public static final String METHOD_NEWREPAIRREPORT = "newRepairReport";
    public static final String METHOD_NEWRETURNLATELY = "newReturnLately";
    public static final String METHOD_NEWSTUDENTLEAVINGSCHOOL = "newStudentLeavingSchool";
    public static final String METHOD_REGISTERACCOUNT = "registerAccount";
    public static final String METHOD_UPDATEAVATAR = "updateAvatar";
    public static final String METHOD_MODIFYPWD = "ModifyPwd";
    public static final String METHOD_GETNO = "getNo";
    public static final String METHOD_GETAVATAR = "getAvatar";
    public static final String METHOD_GETREPAIRDETAILSINFO = "getRepairDetailsInfoBySno";
    public static final String METHOD_GETRLDETAILSINFO = "getRLDetailsInfo ";
    public static final String METHOD_GETSLSDETAILSINFO = "getSLSDetailsInfo";
    public static final String METHOD_GETREPBASICINFOBMP = "getRepairBasicInfoBmpBySno";
    public static final String METHOD_UPDATEREP = "updateRepair";
    public static final String METHOD_UPDATEELS = "updateSLS";
    public static final String METHOD_UPDATERL = "updateReturnLately";
    public static final String METHOD_DELETEREP = "deleteRepair";
    public static final String METHOD_DELETEELS = "deleteSLS";
    public static final String METHOD_DELETERL = "deleteReturnLately";
    public static final String METHOD_GETREPBASICBYMANAGER = "getRepBasicInfoByManager";
    public static final String METHOD_GETSLSBASICBYMANAGER = "getSLSBasicInfoByManager";
    public static final String METHOD_GETRLBASICBYMANAGER = "getRLBasicInfoByManager";

    public static final String PARAMTYPE_SNO = "Sno";
    public static final String PARAMTYPE_ACCOUNT = "account";
    public static final String PARAMTYPE_PWD = "pwd";
    public static final String PARAMTYPE_LEAVEDATE = "leaveDate";
    public static final String PARAMTYPE_BACKDATE = "backDate";
    public static final String PARAMTYPE_REASON = "reason";
    public static final String PARAMTYPE_REPAIRNO = "repairNo";
    public static final String PARAMTYPE_REPAIRAREA = "repairArea";
    public static final String PARAMTYPE_REPAIRPLACE = "repairPlace";
    public static final String PARAMTYPE_REPAIRTYPE = "repairType";
    public static final String PARAMTYPE_DETAIL = "detail";
    public static final String PARAMTYPE_CONTACT = "contact";
    public static final String PARAMTYPE_PHOTO = "photo";
    public static final String PARAMTYPE_RNO = "Rno";
    public static final String PARAMTYPE_RETURNTIME = "returnTime";
    public static final String PARAMTYPE_REPORTDATE = "reportDate";
    public static final String PARAMTYPE_SLSNO = "SLSNo";
    public static final String PARAMTYPE_DELETENO = "deleteNo";
    public static final String PARAMTYPE_ENO = "Eno";
    public static final String PARAMTYPE_TYPE = "type";

    public static final String SQL_REPAIRPLACE = "RepairPlace";
    public static final String SQL_REPAIRTYPE = "RepairType";
    public static final String SQL_DETAIL = "RepairDetail";
    public static final String SQL_CONTACT = "Contact";
    public static final String SQL_RETURNTIME = "returnTime";
    public static final String SQL_REASON = "reason";
    public static final String SQL_LEAVEDATE = "LeaveDate";
    public static final String SQL_BACKDATE = "BackDate";

    private static final String TAG = "WebServiceConnector";
    private static final String WSDL_URI = "http://39.108.113.13/DMS.asmx?wsdl";
    private static final String NAMESPACE = "http://zspirytus.org/";

    /**
     * 发送SOAP请求信息
     *
     * @param methodName 方法名
     * @param paramType  方法参数名
     * @param param      方法参数值
     * @return 封装在ArrayList中的结果
     */
    public static ArrayList<String> executingMethod(String methodName, ArrayList<String> paramType, ArrayList<String> param) {
        String request = getRequest(methodName, paramType, param);
        Log.d(TAG,"requestTest:\t"+request);
        try {
            URL url = new URL(WSDL_URI);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            byte[] bytes = request.getBytes("utf-8");
            con.setDoInput(true); //指定该链接是否可以输入
            con.setDoOutput(false); //指定该链接是否可以输出
            con.setUseCaches(false); //指定该链接是否只用caches
            con.setConnectTimeout(6000); // 设置超时时间
            con.setRequestMethod("POST"); //指定发送方法名，包括Post和Get。
            con.setRequestProperty("Content-Type", "text/xml;charset=utf-8"); //设置（发送的）内容类型
            con.setRequestProperty("SOAPAction", NAMESPACE + methodName); //指定soapAction
            con.setRequestProperty("Content-Length", "" + bytes.length); //指定内容长度

            //发送数据
            OutputStream outStream = con.getOutputStream();
            outStream.write(bytes);
            outStream.flush();
            outStream.close();

            //获取数据
            InputStream inputStream = con.getInputStream();
            String str = InputStreamToString(inputStream);
            return getResult(str);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成SOAP请求信息
     *
     * @param methodName 方法名
     * @param paramType  方法参数名
     * @param params     方法参数值
     * @return SOAP请求信息
     */
    private static String getRequest(String methodName, ArrayList<String> paramType, ArrayList<String> params) {
        String command;
        final String SOAP_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n";
        final String SOAP_END =
                "\n  </soap12:Body>   \n" +
                        "</soap12:Envelope>  ";
        if (paramType != null && params != null) {
            command = "\t\t<" + methodName + " xmlns=\"" + NAMESPACE + "\"> \n";
            for (int i = 0; i < paramType.size(); i++)
                command = command + "\t<" + paramType.get(i) + ">" + params.get(i) + "</" + paramType.get(i) + "> \n";
            command = command + "</" + methodName + ">";
        } else
            command = "\t<" + methodName + " xmlns=\"" + NAMESPACE + "\"/> ";
        return SOAP_HEADER + command + SOAP_END;
    }

    /**
     * 解析SOAP响应信息
     *
     * @return 查询或修改结果
     */
    private static ArrayList<String> getResult(String str) {
        return XmlUtil.getAnalysisResult(str);
    }

    /**
     * InputStream To String
     * @param is InputStream
     * @return String
     */
    private static String InputStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder str = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                str.append(line + "\n");
            }
            Log.d(TAG,"responseTest:\t"+str.toString());
            return str.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e){
            return null;
        }
    }

    /**
     * 判断给定方法是否为单一返回值
     *
     * @param methodName 方法名
     * @return 返回值类型是否为单一返回值
     */
    /*private static boolean isSingleResponse(String methodName) {
        return methodName == METHOD_NEWREPAIRREPORT
                || methodName == METHOD_NEWRETURNLATELY
                || methodName == METHOD_NEWSTUDENTLEAVINGSCHOOL
                || methodName == METHOD_REGISTERACCOUNT
                || methodName == METHOD_UPDATEAVATAR
                || methodName == METHOD_MODIFYPWD
                || methodName == METHOD_GETSNOBYACCOUNT
                || methodName == METHOD_GETAVATAR;
    }*/
}
