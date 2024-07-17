package db.magic.store;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import db.magic.util.Obj;
import db.magic.util.Str;

public class MsgStore {

	private final String msgStoreId; 
	
	final Map<String, Object> map = new TreeMap<>();

	public MsgStore(final String msgStoreId) {
		this.msgStoreId = msgStoreId;
	}

	void addMessage(final String msgId, final Object msg) {
		map.put(msgId, msg);
	}

	public String get(final String msgId) {
		final Object msg = map.get(msgId);
		if (msg == null) {
			return "? " + msgStoreId + "." + msgId + " ?";
		}
		if (msg instanceof List) {
			return String.join("\n", (List)msg);
		}
		return Obj.ofString(msg);
	}

	public String escAmp(final String msgId) {
		return Str.escAmp(get(msgId));
	}

}