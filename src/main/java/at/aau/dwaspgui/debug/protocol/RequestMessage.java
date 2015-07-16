package at.aau.dwaspgui.debug.protocol;


public class RequestMessage extends Message {
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = "get";
	
	/** the type of the request */
	private final RequestType requestType;

	/**
	 * Creates a new request message with the given type.
	 * @param request The type of the request.
	 */
	public RequestMessage(RequestType request) {
		this.requestType = request;
	}
	
	@Override
	protected String serialize() {
		return MESSAGE_IDENTIFIER + DELIMITER_PART + requestType.getCommand();
	}
	
	protected static RequestMessage parseFromString(String message) {
		// the debugger must not perform any requests on the GUI
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Contains all requests.
	 * @author Philip Gasteiger
	 */
	public enum RequestType {
		GET_CORE("core")
	  , GET_HISTORY("history")
	  , GET_QUERY("query")
	  ;
		
		/** protocol text of the command */
		private final String command;
		
		RequestType (String command) {
			this.command = command;
		}
		
		/**
		 * Get the protocol text of the command.
		 * @return The protocol text.
		 */
		String getCommand() {
			return this.command;
		}
	}
}
