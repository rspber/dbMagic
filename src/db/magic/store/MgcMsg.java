package db.magic.store;

import java.util.Map;
import java.util.TreeMap;

import db.magic.yaml.YamlIn;
import db.magic.yaml.Yamlet;

public class MgcMsg extends Yamlet {

	static final String MESSAGES = "messages.yaml";

	private final Map<String, MsgStore> msgStores = new TreeMap<>();	// <MsgId, MsgStore>

	private static MgcMsg fDefault;
	public static MgcMsg getDefault() {
		if( fDefault == null ) {
			fDefault = new MgcMsg();
			fDefault.initialization(MESSAGES);
		}
		return fDefault;
	}

	@Override
	public void clear() {
	}

	@Override
	public void setModified(final boolean value) {
	}

	@Override
	public boolean wasModified() {
		return false;
	}

	public static MsgStore getMsgStore(final String msgStoreId) {
		final MsgStore msgStore = getDefault().msgStores.get(msgStoreId);
		if (msgStore == null) {
			throw new RuntimeException("No " + msgStoreId + " section in " + MESSAGES);
		}
		return msgStore;
	}

	MsgStore newMsgStore(final String msgStoreId) {
		MsgStore msgStore = msgStores.get(msgStoreId);
		if (msgStore == null) {
			msgStore = new MsgStore(msgStoreId);
			msgStores.put(msgStoreId, msgStore);
		}
		return msgStore;
	}

	@Override
	public void load(final Object yamlObject) {
		YamlIn.loadSections(yamlObject, (msgStoreId, msgsMap) -> {
			final MsgStore msgStore = newMsgStore((String)msgStoreId);
			msgsMap.forEach( (msgId, msg) -> {
				msgStore.addMessage(msgId, msg);
			});
		});
	}

	@Override
	public String toYaml() {
		throw new RuntimeException("Attempt to write " + MESSAGES);
/*
		final StringBuilder sb = new StringBuilder();
		msgStores.forEach( (msgStoreId, msgStore) -> {
			YamlOut.yaml(sb, 0, msgStoreId, null);
			msgStore.map.forEach( (msgId, msg) -> {
				YamlOut.yaml(sb, 2, msgId, msg);
			});
		});
		return sb.toString();
*/
	}

}