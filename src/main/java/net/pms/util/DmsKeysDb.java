package net.pms.util;

public class DmsKeysDb implements DbHandler { //TODO: (Nad) Remove
	private FileDb db;

	public DmsKeysDb() {
		db = new FileDb(this);
		db.setMinCnt(2);
		db.init();
	}

	public void set(String key, String val) {
		db.add(key, val);
	}

	public String get(String key) {
		return (String) db.get(key);
	}

	@Override
	public Object create(String[] args) {
		return args[1];
	}

	@Override
	public String[] format(Object obj) {
		return new String[]{(String) obj};
	}

	@Override
	public String name() {
		return "DMSKeys.db";
	}
}
