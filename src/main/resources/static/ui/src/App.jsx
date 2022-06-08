import {HashRouter as BrowserRouter, Route, Routes} from "react-router-dom";
import './App.css';
import "./assets/css/loader.css";
import './assets/css/errorPageStyle.css';
import './assets/css/tableStyle.css';
import './assets/css/fontStyle.css';
import './assets/css/modalStyle.css';
import './assets/css/drop-file-input.css';

import Login from "./pages/auth/Login";
import Dashboard from "./pages/menu/Dashboard";
import Error401 from "./pages/error/Error401";
import Error404 from "./pages/error/Error404";
import Error500 from "./pages/error/Error500";
import {Fragment} from "react";
import RecoverPasswordCode from "./pages/auth/RecoverPasswordCode";
import RecoverPasswordUsername from "./pages/auth/RecoverPasswordUsername";

function App() {

    return (
        <Fragment>
            <BrowserRouter>
                <Routes>
                    <Route exact path={"/"} element={<Login/>}/>
                    <Route exact path={"/login"} element={<Login/>}/>
                    <Route exact path="/recover-password" element={<RecoverPasswordUsername/>}/>
                    <Route exact path="/recover-password/:id/:email" element={<RecoverPasswordCode/>}/>
                    <Route path="/dashboard" element={<Dashboard/>}/>
                    <Route exact path="/401" element={<Error401/>}/>
                    <Route exact path="/500" element={<Error500/>}/>
                    <Route exact path="*" element={<Error404/>}/>
                </Routes>
            </BrowserRouter>
        </Fragment>

    )
}

export default App
