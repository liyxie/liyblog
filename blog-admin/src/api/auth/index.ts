import request from "@/utils/request";
import { AxiosPromise } from "axios";
import { CaptchaResult, LoginData, LoginResult } from "./types";

/**
 * 登录API
 *
 * @param data {LoginData}
 * @returns
 */
export function loginApi(data:any): AxiosPromise {
  const formData = new FormData();
  formData.append("username", data.username);
  formData.append("password", data.password);
  formData.append("captchaKey", data.captchaKey || "");
  formData.append("captchaCode", data.captchaCode || "");
  formData.append("nonceStr", data.nonceStr || "222");
  formData.append("value", data.value || "111");
  formData.append("rememberMe", data.rememberMe || false);
  return request({
    url: "/login",
    method: "post",
    data: formData,
  });
}

/**
 * 注销API
 */
export function logoutApi() {
  return request({
    url: "/logout",
    method: "get",
  });
}

/**
 * 获取验证码
 */
export function getCaptchaApi(): AxiosPromise<CaptchaResult> {
  return request({
    url: "/verify",
    method: "get",
  });
}
