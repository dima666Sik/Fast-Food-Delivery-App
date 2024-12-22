import axios from "axios";

export const register = async (userData) => {
	try {
		const response = await axios.post(
			`${process.env.REACT_APP_SERVER_API_URL}api/v2/auth/register`,
			userData
		);
		return response;
	} catch (error) {
		console.log(error.response.data.message);
		throw error;
	}
};
