import axios from "axios";

export const logout = async (accessToken) => {
	try {
		const response = await axios.post(
			`${process.env.REACT_APP_SERVER_API_URL}api/v2/auth/logout`,
			{},
			{
				headers: {
					Authorization: "Bearer " + accessToken,
				},
			}
		);
		return response;
	} catch (error) {
		console.log(error.response.data.message);
		throw error;
	}
};
