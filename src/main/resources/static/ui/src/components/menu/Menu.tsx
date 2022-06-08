import {Fragment, useEffect, useState} from "react";
import {AiOutlineClose, AiOutlineMenu, AiOutlineUser} from "react-icons/ai";
import {CgLogIn} from "react-icons/cg";
import Profile from "./Profile";
import {useNavigate} from "react-router-dom";

/**
 *  @description: Menu component
 * @param props list items
 */
const Menu = (props) => {
    const [listItems, setListItems] = useState([]); // listItems of items
    const [component, setComponent] = useState(props.component);// component to render
    const [isOpen, setIsOpen] = useState("");// is menu aside menu
    const [titleContent, setTitleContent] = useState(props.text); // title content

    const navigate = useNavigate();

    /**
     * @description: aside menu close
     */
    const offAside = () => {
        setIsOpen("d-none");
    }

    /**
     * @description: aside menu open
     */
    const onAside = () => {
        setIsOpen("d-block position-fixed");
    }

    /**
     * @description: component profile
     */
    const profile = () => {
        for (let content of props.contents) {
            document.getElementById(content.text).classList.remove("item-aside-active");// remove item active
        }

        setComponent(<Profile userData={props.userData}/>);// set component profile
        setTitleContent("Profile");// set title content
    }

    /**
     * @description: Active item
     * @param item item
     */
    function itemActive(item) {
        setComponent(item.component);
        setTitleContent(item.text);
        props.contents.map(cont => cont.text !== item.text
            ? document.getElementById(cont.text).classList.remove("item-aside-active")
            : document.getElementById(cont.text).classList.add("item-aside-active"));
    }

    function Item(props) {
        return (
            <Fragment>
                <div id={props.object.text}//item
                     className={"item-aside border-radius-main font-size-20 " + (props.object.text === props.text ? "item-aside-active" : "")}
                     onClick={(event: any) => itemActive(props.object)}>
                    <i>{props.object.icon}</i>
                    <span className={"font-size-18 m-2"}>{props.object.text}</span>
                </div>
            </Fragment>
        )
    }

    /**
     * @description: Set initial state
     */
    useEffect(() => {
        let aux = [];
        props.contents.map((item, id) => {
            aux.push(<Item key={id} object={item} text={props.text}/>)
        });
        setListItems(aux);// set listItems items
    }, []);

    return (
        <Fragment>
            <div>
                <nav className="nav-bar-aside align-items-center">
                    <span className={"nav-bar-aside-items-title"}>{"Pages / " + titleContent}</span>
                    <ul className={"nav-bar-aside-items font-size-18"}>
                        <li className={"mt-3 d-flex"}>
                            <a onClick={profile} className={"nav-bar-aside-items-item cursor-pointer"}><AiOutlineUser/>
                                <span className={"ms-1"}>{props.userData?.username}</span></a>
                        </li>

                        <li className={"mt-3 d-flex"}>
                            <a className={"nav-bar-aside-items-item cursor-pointer"}
                               onClick={() => {
                                   localStorage.clear(), navigate("/login")
                               }}><CgLogIn/>
                                <span className={"ms-1"}>Sign off</span>
                            </a>
                        </li>

                        <li className={"mt-3 d-flex"}>
                            <a><AiOutlineMenu className={"nav-bar-aside-items-item cursor-pointer menu-item-aside"}
                                              onClick={onAside}/>
                            </a>
                        </li>
                    </ul>
                </nav>

                <div className={"div-content-aside row"}>
                    {component}
                </div>
            </div>

            <aside className={"aside border-radius-main box-shadow-main " + isOpen}>
                <i className={"fas fa-times " +
                    "p-2 opacity-5 cursor-pointer " +
                    "position-absolute end-0 top-0 d-xl-none text-grey "}
                   onClick={offAside}>
                    <AiOutlineClose/></i>
                <div className={"d-flex m-3 mb-4"}>
                    {props.icon}
                    <h1
                        className={"ms-1 font-size-35 text-color-grey"}>{props.title}
                    </h1>
                </div>

                {listItems}
            </aside>
        </Fragment>
    )
}

export default Menu;