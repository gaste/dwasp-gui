package at.aau.dwaspgui.viewmodel.query;

import at.aau.dwaspgui.domain.QueryAnswer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * View model for a single query.
 * @author Philip Gasteiger
 */
public class QueryViewModel {
	public static final QueryAnswer DEFAULT_ANSWER = QueryAnswer.UNKNOWN;

	private final StringProperty atom;
	private final ObjectProperty<QueryAnswer> answer;
	
	public QueryViewModel(String atom) {
		this.atom = new SimpleStringProperty(atom);
		this.answer = new SimpleObjectProperty<QueryAnswer>(DEFAULT_ANSWER);
	}
	
	public StringProperty atomProperty() { return atom; }
	public String getAtom() { return atom.get(); }
	
	public ObjectProperty<QueryAnswer> answerProperty() { return answer; }
	public QueryAnswer getAnswer() { return this.answer.get(); }
	public void setAnswer(QueryAnswer answer) { this.answer.set(answer); }
}
