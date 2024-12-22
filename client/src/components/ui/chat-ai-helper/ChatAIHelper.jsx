import React, { useState, useEffect, useRef } from "react";
import axios from "axios";
import { Spinner } from "react-bootstrap";
import { useSelector, useDispatch } from "react-redux";
import { chatAIActions } from "../../../redux/store/chatAISlice";
import "./ChatAIHelper.css";

const ChatAIHelper = () => {
	const [messages, setMessages] = useState([]); // История сообщений
	const [newMessage, setNewMessage] = useState(""); // Сообщение пользователя
	const [isLoading, setIsLoading] = useState(false); // Индикатор загрузки
	const chatContainerRef = useRef(null); // Ссылка на контейнер чата для автоскролла
	const dispatch = useDispatch();
	const isChatVisible = useSelector((state) => state.chatAI.isChatVisible);
	console.log("It is work");
	const toggleChatVisibility = () => {
		dispatch(chatAIActions.toggleVisible());
	};

	const isAuthenticated = useSelector((state) => state.user.isAuthenticated);

	const userFirstName = useSelector((state) => state.user.firstName);

	// Функция для получения истории сообщений
	const fetchMessages = async () => {
		try {
			const response = await axios.get(
				"http://localhost:8080/api/v2/chat-ai-helper/get-chat-history?user_id=1"
			);
			console.log(response.data);
			setMessages(response.data.messages);
		} catch (error) {
			console.error("Error fetching messages:", error);
		}
	};

	// Обновляем историю сообщений при загрузке компонента
	useEffect(() => {
		fetchMessages();
	}, []);

	// Автоскролл к последнему сообщению
	useEffect(() => {
		if (chatContainerRef.current) {
			chatContainerRef.current.scrollTop =
				chatContainerRef.current.scrollHeight;
		}
	}, [messages]);

	// Обработчик отправки нового сообщения
	const handleSendMessage = async (e) => {
		e.preventDefault();

		if (!newMessage.trim()) return; // Проверка на пустое сообщение

		setIsLoading(true); // Включаем индикатор загрузки

		try {
			await axios.post("http://localhost:8080/api/v2/chat-ai-helper/message", {
				user_id: 1,
				message: newMessage,
			});

			setNewMessage(""); // Сбрасываем поле ввода
			await fetchMessages(); // Обновляем историю сообщений
		} catch (error) {
			console.error("Error sending message:", error);
		} finally {
			setIsLoading(false); // Выключаем индикатор загрузки
		}
	};

	return (
		<div className="chat-wrapper">
			<button className="toggle-chat-button" onClick={toggleChatVisibility}>
				{isChatVisible ? "Close Chat" : "Open Chat"}
			</button>

			{isChatVisible && (
				<div className="chat-container">
					<div className="chat-header">
						<h2>Chat AI Helper</h2>
					</div>

					<div className="chat-messages" ref={chatContainerRef}>
						{messages.length === 0 && !isLoading ? (
							<p className="no-messages">No messages yet.</p>
						) : (
							messages
								.filter((msg) => msg.role !== "system")
								.map((msg, index) => {
									// Извлечение первой буквы имени или "В" для ассистента
									const initial =
										msg.role === "user"
											? isAuthenticated
												? userFirstName.charAt(0).toUpperCase()
												: "G"
											: "В";

									return (
										<div
											key={index}
											className={`chat-message ${
												msg.role === "user" ? "user" : "assistant"
											}`}
										>
											<div className="message-icon">{initial}</div>
											<p>{msg.content}</p>
										</div>
									);
								})
						)}

						{isLoading && (
							<div className="loading-spinner">
								<Spinner />
							</div>
						)}
					</div>

					<form className="chat-form" onSubmit={handleSendMessage}>
						<input
							type="text"
							placeholder="Type your message..."
							value={newMessage}
							onChange={(e) => setNewMessage(e.target.value)}
							disabled={isLoading}
						/>
						<button type="submit" disabled={isLoading || !newMessage.trim()}>
							Send
						</button>
					</form>
				</div>
			)}
		</div>
	);
};

export default ChatAIHelper;
