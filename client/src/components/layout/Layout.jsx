import React from "react";
import Routers from "../../routes/Routers";

import Footer from "../footer/Footer";
import Header from "../header/Header";
import Carts from "../ui/carts/cart/Carts";
import { useSelector } from "react-redux";

const Layout = () => {
	const visibleCart = useSelector((state) => state.cartUI.cartIsVisible);
	return (
		<div>
			<Header />

			{visibleCart && <Carts />}

			<div>
				<Routers />
			</div>
			<Footer />
		</div>
	);
};

export default Layout;
