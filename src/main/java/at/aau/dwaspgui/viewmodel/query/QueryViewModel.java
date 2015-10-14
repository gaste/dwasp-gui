/*
 *  Copyright 2015 Philip Gasteiger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
