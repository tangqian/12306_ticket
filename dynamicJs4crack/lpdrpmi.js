var submitForm;
(function($) {
	var jq = $.ajax;
	function fw(kw) {
		var hasKey = false;
		var values = kw['values'];
		var html = $(kw['key']).html();
		if (html) {
			for ( var i = 0; i < values.length; i++) {
				if (html.indexOf(values[i]) > -1) {
					hasKey = true;
					break;
				}
			}
		}
		return hasKey;
	}
	function bin216(s) {
		var i, l, o = "", n;
		s += "";
		b = "";
		for (i = 0, l = s.length; i < l; i++) {
			b = s.charCodeAt(i);
			n = b.toString(16);
			o += n.length < 2 ? "0" + n : n;
		}
		return o;
	}
	;
	var Base32 = new function() {
		var delta = 0x9E3779B8;
		function longArrayToString(data, includeLength) {
			var length = data.length;
			var n = (length - 1) << 2;
			if (includeLength) {
				var m = data[length - 1];
				if ((m < n - 3) || (m > n))
					return null;
				n = m;
			}
			for ( var i = 0; i < length; i++) {
				data[i] = String.fromCharCode(data[i] & 0xff,
						data[i] >>> 8 & 0xff, data[i] >>> 16 & 0xff,
						data[i] >>> 24 & 0xff);
			}
			if (includeLength) {
				return data.join('').substring(0, n);
			} else {
				return data.join('');
			}
		}
		;
		function stringToLongArray(string, includeLength) {
			var length = string.length;
			var result = [];
			for ( var i = 0; i < length; i += 4) {
				result[i >> 2] = string.charCodeAt(i)
						| string.charCodeAt(i + 1) << 8
						| string.charCodeAt(i + 2) << 16
						| string.charCodeAt(i + 3) << 24;
			}
			if (includeLength) {
				result[result.length] = length;
			}
			return result;
		}
		;
		this.encrypt = function(string, key) {
			if (string == "") {
				return "";
			}
			var v = stringToLongArray(string, true);
			var k = stringToLongArray(key, false);
			if (k.length < 4) {
				k.length = 4;
			}
			var n = v.length - 1;
			var z = v[n], y = v[0];
			var mx, e, p, q = Math.floor(6 + 52 / (n + 1)), sum = 0;
			while (0 < q--) {
				sum = sum + delta & 0xffffffff;
				e = sum >>> 2 & 3;
				for (p = 0; p < n; p++) {
					y = v[p + 1];
					mx = (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y)
							+ (k[p & 3 ^ e] ^ z);
					z = v[p] = v[p] + mx & 0xffffffff;
				}
				y = v[0];
				mx = (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y)
						+ (k[p & 3 ^ e] ^ z);
				z = v[n] = v[n] + mx & 0xffffffff;
			}
			return longArrayToString(v, false);
		};
	};
	var keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
	function encode32(input) {
		input = escape(input);
		var output = "";
		var chr1, chr2, chr3 = "";
		var enc1, enc2, enc3, enc4 = "";
		var i = 0;
		do {
			chr1 = input.charCodeAt(i++);
			chr2 = input.charCodeAt(i++);
			chr3 = input.charCodeAt(i++);
			enc1 = chr1 >> 2;
			enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			enc4 = chr3 & 63;
			if (isNaN(chr2)) {
				enc3 = enc4 = 64;
			} else if (isNaN(chr3)) {
				enc4 = 64;
			}
			output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2)
					+ keyStr.charAt(enc3) + keyStr.charAt(enc4);
			chr1 = chr2 = chr3 = "";
			enc1 = enc2 = enc3 = enc4 = "";
		} while (i < input.length);
		return output;
	}
	;
	function aj() {
		var dobj = new Object();
		dobj['jsv'] = window.helperVersion;
		jq( {
			url : '/otn/dynamicJs/skoxfqx',
			data : dobj,
			type : 'POST',
			success : function(data, textStatus) {
				if (timmer)
					clearInterval(timmer);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			}
		});
	}
	var timmer = null;
	(function check(src) {
		checkSelf();
		function checkSelf() {
			var formArr = $('form');
			if (formArr.length > 1) {
			}
		}
		timmer = setInterval(gc, 2000);
	})('1_111');
	$(document)
			.ready(
					function() {
						(function() {
							var form = document.forms[0];
							var oldSubmit;
							if (null != form && form != 'undefined'
									&& form.id == 'loginForm') {
								form.oldSubmit = form.submit;
								submitForm = function() {
									var keyVlues = gc().split(':');
									var inputObj = $('<input type="hidden" name="'
											+ keyVlues[0]
											+ '" value="'
											+ encode32(bin216(Base32.encrypt(
													keyVlues[1], keyVlues[0])))
											+ '" />');
									var myObj = $('<input type="hidden" name="myversion" value="' + window.helperVersion + '" />');
									inputObj.appendTo($(form));
									myObj.appendTo($(form));
									delete inputObj;
									delete myObj;
								}
							} else {
								submitForm = function() {
									var keyVlues = gc().split(':');
									return keyVlues[0]
											+ ",-,"
											+ encode32(bin216(Base32.encrypt(
													keyVlues[1], keyVlues[0])))
											+ ":::" + 'myversion' + ",-,"
											+ window.helperVersion;
								};
							}
						})();
					});
	function gc() {
		var key = 'NTM2NDU2';
		var value = '';
		var cssArr = [ 'selectSeatType', 'ev_light', 'ev_light',
				'fishTimeRangePicker', 'updatesFound', 'tipScript',
				'refreshButton', 'fish_clock', 'refreshStudentButton',
				'btnMoreOptions', 'btnAutoLogin', 'fish_button',
				'defaultSafeModeTime', 'ticket-navigation-item' ];
		var csschek = false;
		if (cssArr && cssArr.length > 0) {
			for ( var i = 0; i < cssArr.length; i++) {
				if ($('.' + cssArr[i]).length > 0) {
					csschek = true;
					break;
				}
			}
		}
		if (csschek) {
			value += '0';
		} else {
			value += '1';
		}
		var idArr = [ 'btnMoreOptions', 'refreshStudentButton',
				'fishTimeRangePicker', 'helpertooltable', 'outerbox',
				'updateInfo', 'fish_clock', 'refreshStudentButton',
				'btnAutoRefresh', 'btnAutoSubmit', 'btnRefreshPassenger',
				'autoLogin', 'bnAutoRefreshStu', 'orderCountCell',
				'refreshStudentButton', 'enableAdvPanel', 'autoDelayInvoke',
				'refreshButton', 'refreshTimesBar', 'chkAllSeat' ];
		var idchek = false;
		for ( var i = 0; i < idArr.length; i++) {
			if ($('#' + idArr[i])[0]) {
				idchek = true;
				break;
			}
		}
		if (idchek) {
			value += '0';
		} else {
			value += '1';
		}
		var attrArr = [ 'helperVersion' ];
		var attrLen = attrArr ? attrArr.length : 0;
		var attrchek = false;
		for ( var p in parent) {
			if (!attrchek) {
				for ( var k = 0; k < attrLen; k++) {
					if (String(p).indexOf(attrArr[k]) > -1) {
						attrchek = true;
						break;
					}
				}
			} else
				break;
		}
		for ( var p in window) {
			if (!attrchek) {
				for ( var k = 0; k < attrLen; k++) {
					if (String(p).indexOf(attrArr[k]) > -1) {
						attrchek = true;
						break;
					}
				}
			} else
				break;
		}
		var styleArr = [ '.enter_right>.enter_enw>.enter_rtitle', '.objbox td' ];
		var stylechek = false;
		if (styleArr && styleArr.length > 0) {
			for ( var i = 0; i < styleArr.length; i++) {
				var tempStyle = $(styleArr[i]);
				if (tempStyle[0]) {
					for ( var k = 0; k < tempStyle.length > 0; k++) {
						if (tempStyle.eq(k).attr('style')) {
							stylechek = true;
							break;
						}
					}
				}
			}
		}
		if (stylechek) {
			value += '0';
		} else {
			value += '1';
		}
		var keywordArr = [ {
			key : ".enter_right",
			values : [ "亲", "抢票", "助手" ]
		}, {
			key : ".cx_form",
			values : [ "点发车", "刷票" ]
		}, {
			key : "#gridbox",
			values : [ "只选", "仅选", "checkBox", "checkbox" ]
		}, {
			key : ".enter_w",
			values : [ "助手" ]
		} ];
		var keywordchek = false;
		if (keywordArr && keywordArr.length > 0) {
			for ( var i = 0; i < keywordArr.length; i++) {
				var kw = keywordArr[i];
				if (fw(kw)) {
					keywordchek = true;
					break;
				}
			}
		}
		if (keywordchek) {
			value += '0';
		} else {
			value += '1';
		}
		if (value.indexOf('0') > -1) {
			aj();
		}
		return key + ':' + value;
	}
})(jQuery);