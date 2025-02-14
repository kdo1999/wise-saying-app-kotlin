package java.wisesaying.domain;

public class WiseSaying {
	private Long id;
	private String wiseSaying;
	private String writer;

	public WiseSaying(Long id, String wiseSaying, String writer) {
		this.id = id;
		this.wiseSaying = wiseSaying;
		this.writer = writer;
	}

	public WiseSaying(String wiseSaying, String writer) {
		this.wiseSaying = wiseSaying;
		this.writer = writer;
	}

	public static WiseSaying createWiseSaying(String wiseSaying, String writer) {
		return new WiseSaying(wiseSaying, writer);
	}

	public Long getId() {
		return id;
	}

	public String getWiseSaying() {
		return wiseSaying;
	}

	public String getWriter() {
		return writer;
	}

	public void fetch(String updateWiseSaying, String updateWriter) {
		if (!this.wiseSaying.equals(updateWiseSaying)) {
			this.wiseSaying = updateWiseSaying;
		}

		if (!this.writer.equals(updateWriter)) {
			this.writer = updateWriter;
		}
	}

}
