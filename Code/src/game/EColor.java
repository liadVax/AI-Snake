package game;

public enum EColor {
	blue, cyan, red, orange, white, head;

	public String StringFullName() {
		switch (this) {
		case blue:
			return "Blue";
		case cyan:
			return "Cyan";
		case red:
			return "Red";
		case orange:
			return "Orange";
		case white:
			return "White";
		default:
			return "Nan";
		}
	}

	@Override
	public String toString() {
		if (this == blue) {
			return "B";
		}
		if (this == white) {
			return "_";
		}
		if (this == cyan) {
			return "C";
		}
		if (this == red) {
			return "R";
		}
		if (this == orange) {
			return "O";
		}
		if (this == head) {
			return "H";
		}
		return null;
	}



}
