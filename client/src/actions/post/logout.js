import axios from "axios";

export const logout = async (accessToken) => {
	try {
		const response = await axios.post(
			`${process.env.REACT_APP_SERVER_API_URL}api/v1/auth/logout`,
			{},
			{
				headers: {
					Authorization: "Bearer " + accessToken,
				},
			}
		);
		return response;
	} catch (error) {
		// console.error(error);
		throw error; // пробрасываем ошибку выше для обработки
	}
};
