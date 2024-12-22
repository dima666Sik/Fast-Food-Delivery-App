import axios from "axios";

export const refresh = async (accessToken) => {
	try {
		const response = await axios.post(
			`${process.env.REACT_APP_SERVER_API_URL}api/v2/auth/refresh-tokens`,
			{},
			{
				headers: {
					Authorization: "Bearer " + accessToken,
				},
			}
		);
		console.log("Access token: ", accessToken);
		return response;
	} catch (error) {
		console.log(error.response.data.message);
		throw error;
	}
};
