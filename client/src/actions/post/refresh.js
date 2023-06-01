import axios from "axios";

export const refresh = async (accessToken) => {
	try {
		const response = await axios.post(
			`${process.env.REACT_APP_SERVER_API_URL}api/v1/auth/refresh-tokens`,
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
		throw error; // пробрасываем ошибку выше для обработки в setLikes
	}
};
