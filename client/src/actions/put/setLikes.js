import axios from "axios";

export const setLike = async (idProduct, likes, accessToken) => {
	try {
		await axios.put(
			`${process.env.REACT_APP_SERVER_API_URL}api/v2/product/private/product-like/set-likes-product`,
			{ product_id: idProduct, likes },
			{
				headers: {
					Authorization: "Bearer " + accessToken,
				},
			}
		);
	} catch (error) {
		console.log(error.response.data.message);
		throw error;
	}
};
