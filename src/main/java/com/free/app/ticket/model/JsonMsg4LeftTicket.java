package com.free.app.ticket.model;

import java.util.List;

/**
 * 
 * 火车信息数据实体类
 * 
 */
public class JsonMsg4LeftTicket extends JsonMsgSuper {

	private List<TrainQueryInfo> data;

	public List<TrainQueryInfo> getData() {
		return data;
	}

	public void setData(List<TrainQueryInfo> data) {
		this.data = data;
	}

	public class TrainQueryInfo {
		private TrainInfo queryLeftNewDTO;

		private String secretStr;

		private String buttonTextInfo;

		public TrainInfo getQueryLeftNewDTO() {
			return queryLeftNewDTO;
		}

		public void setQueryLeftNewDTO(TrainInfo queryLeftNewDTO) {
			this.queryLeftNewDTO = queryLeftNewDTO;
		}

		public String getSecretStr() {
			return secretStr;
		}

		public void setSecretStr(String secretStr) {
			this.secretStr = secretStr;
		}

		public String getButtonTextInfo() {
			return buttonTextInfo;
		}

		public void setButtonTextInfo(String buttonTextInfo) {
			this.buttonTextInfo = buttonTextInfo;
		}

		@Override
		public String toString() {
			return "TrainQueryInfo [buttonTextInfo=" + buttonTextInfo
					+ ", secretStr=" + secretStr + ", queryLeftNewDTO="
					+ queryLeftNewDTO + "]";
		}
		
		
	}

}
