import axios from "axios";

export const setLike = async (idProduct, likes, accessToken) => {
	try {
		await axios.put(
			`${process.env.REACT_APP_SERVER_API_URL}api/v1/private/product-like/set-like-product`,
			{ product_id: idProduct, likes },
			{
				headers: {
					Authorization: "Bearer " + accessToken,
				},
			}
		);
	} catch (error) {
		throw error;
	}
};
