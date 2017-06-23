package com.geetion.puputuan.filter;

import com.geetion.puputuan.utils.EmojiCharacterUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by guodikai on 2016/11/21.
 */
public class RequestFilter extends OncePerRequestFilter {

    public String filter(HttpServletRequest request, String input) {

        String ret = input;

        //ios客户端请求参数值可能为(null)服务端过滤掉当null处理即可

        if (input == null || input.trim().equals("(null)")) {
            ret=null;
            return ret;
        }

        return EmojiCharacterUtil.emojiConvert1(input);

    }

    @Override
    protected void doFilterInternal(final HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {

        filterChain.doFilter(new HttpServletRequestWrapper(httpServletRequest) {
            @Override
            public String getParameter(String name) {
                String value = super.getParameter(name);
                return filter(this, value);

            }

            @Override
            public String[] getParameterValues(String name) {
                String[] values = super.getParameterValues(name);
                if (values == null) {
                    return null;
                }
                for (int i = 0; i < values.length; i++) {
                    values[i] = filter(this, values[i]);
                }
                return values;
            }
        }, httpServletResponse);
    }
}
