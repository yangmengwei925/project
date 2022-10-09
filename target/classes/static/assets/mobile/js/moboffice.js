/*! v1.0.0 | moboffice.js | (c) 2016, 2018 Beijing zhuozheng zhiyuan software, Inc.*/
function po_uuid(len, radix) {
    var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    var uuid = [],
    i;
    radix = radix || chars.length;
    if (len) {
        for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random() * radix]
    } else {
        var r;
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
        uuid[14] = '4';
        for (i = 0; i < 36; i++) {
            if (!uuid[i]) {
                r = 0 | Math.random() * 16;
                uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8: r]
            }
        }
    }
    return uuid.join('')
}
function obj2str(data) {
    data = data || {};
    var res = [];
    for (var key in data) {
        res.push(encodeURIComponent(key) + "=" + encodeURIComponent(data[key]))
    }
    return res.join("&")
}
function po_ajax(option) {
    var params = obj2str(option.data);
    var xmlhttp;
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest()
    } else {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP")
    } if (option.type.toUpperCase() === "GET") {
        xmlhttp.open("GET", option.url + "?" + params);
        xmlhttp.withCredentials = option.withCred;
        xmlhttp.send()
    } else {
        xmlhttp.open("POST", option.url);
        xmlhttp.withCredentials = option.withCred;
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xmlhttp.send(params)
    }
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState === 4) {
            if (xmlhttp.status >= 200 && xmlhttp.status < 300 || xmlhttp.status === 304) {
                option.success(xmlhttp.responseText, "success")
            } else {}
        }
    }
}
var MobAPP = {
    getRootPath: function() {
		var pathName = "";
        var mob_js_main = document.getElementById('mob_js_main');
        if (mob_js_main != null) {
            pathName = document.getElementById('mob_js_main').src
        } else {
            var aScript = document.getElementsByTagName("script");
            for (var i = 0; i < aScript.length; i++) {
                if (aScript[i].src.indexOf("moboffice.js") > -1) {
                    pathName = aScript[i].src
                }
            }
        }
        var index = pathName.indexOf("/moboffice.js");
        return pathName.substr(0, index)
    },
    openWindow: function(strURL) {
        if ((strURL == null) || (strURL == "")) {
            alert("The parameter strURL of openWindow() cannot be null or empty.");
            return
        }
        if (strURL.charAt(0) != '/') {
            var strLower = strURL.toLowerCase();
            if ((strLower.substr(0, 7) == "http://") || (strLower.substr(0, 8) == "https://")) {} else {
                var pathName = window.location.href;
                var index = pathName.lastIndexOf("/");
                strURL = pathName.substr(0, index + 1) + strURL
            }
        } else {
            var pathName = window.location.href;
            var index = pathName.indexOf(window.location.pathname);
            strURL = pathName.substr(0, index) + strURL
        }
		
		po_ajax({
			url: this.getRootPath() + "/mobserver.zz",
			type: "POST",
			data: {
				Info: "MobAPPLink",
				pageurl: strURL
			},
			success: function(data) {
				location.href = data
			}
		})
    }
};