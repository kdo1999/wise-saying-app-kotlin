package java.infrastructure.wisesaying;

public class IdGeneration {
	private Long id;

	public IdGeneration(Long id) {
		this.id = id;
	}

	public Long generationId() {
		return ++id;
	}

	public void resetId() {
		id = 0L;
	}
}
