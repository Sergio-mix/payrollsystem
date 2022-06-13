import {useState, useEffect, Fragment} from "react";
import {useLocation, useNavigate} from 'react-router-dom';
import Menu from "../../components/menu/Menu";
import {AiOutlineBarChart, AiOutlineTeam, AiOutlineCalendar, AiFillLock, AiOutlineTable} from "react-icons/ai";
import {SiMicrosoftexcel} from "react-icons/si";
import {MdCloudCircle} from "react-icons/md";
import Users from "../../components/users/Users";
import Traceability from "../../components/traceability/Traceability";
import {getUserId} from "../../services/userService";
import ChargingPad from "../../components/util/ChargingPad";
import Modal from "../../components/util/Modal";
import Statistics from "../../components/statistics/Statistics";
import PayrollFiles from "../../components/payroll/PayrollFiles";
import PayrollData from "../../components/payroll/PayrollData";


/**
 * @description Dashboard page
 */
const Dashboard = () => {
    const [component, setComponent] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [contentModal, setContentModal] = useState(null);
    const [isCloseModal, setIsCloseModal] = useState(true);
    const modal = {open: openModal, openIsCloseNot: openModalIsCloseNot, close: closeModal};

    const location = useLocation();
    const navigate = useNavigate();

    function openModal(content) {
        setShowModal(true);
        setIsCloseModal(true);
        setContentModal(content);
    }

    function openModalIsCloseNot(content) {
        setShowModal(true);
        setIsCloseModal(false);
        setContentModal(content);
    }

    function closeModal() {
        setShowModal(false);
    }

    function error401() {
        modal.openIsCloseNot(<div>
            <p className={"font-size-30 text-color-grey"}><AiFillLock/> Expired Session</p>
        </div>);

        setTimeout(() => {
            localStorage.clear();
            navigate("/login");
        }, 2000);
    }

    /**
     * @description user by id
     */
    const userId = async (id) => await getUserId(id).then(user => {
        return user.data;
    }).catch(error => {
        switch (error.response.status) {
            case 401:
                error401();
                break;
            case 404:
                navigate("*");
                break;
            case 500:
                navigate("500");
                break;
            case 0:
                error401();
                break;
        }
    });

    const load = async (id) => await userId(id); //user by id

    const admin = [
        {
            icon: <AiOutlineBarChart/>,
            text: "Statistics",
            component: <Statistics/>
        },
        {
            icon: <AiOutlineTeam/>,
            text: "Users",
            component: <Users modal={modal}/>
        },
        {
            icon: <AiOutlineCalendar/>,
            text: "Traceability",
            component: <Traceability modal={modal}/>
        }
    ];

    const manager = [
        {
            icon: <AiOutlineTable/>,
            text: "Payroll Data",
            component: <PayrollData modal={modal}/>
        }
    ];


    const user = [
        {
            icon: <SiMicrosoftexcel/>,
            text: "PayrollFile",
            component: <PayrollFiles modal={modal}/>
        }
    ];

    /**
     * @description component by user
     * @param userData user data
     */
    function assignFeatures(userData) {
        let aux = [];

        let authorities = userData.authorities;

        authorities.map((authority) => {
            return authority.roleCode;
        })

        if (authorities.find(authority => authority.roleCode === "ADMIN") !== undefined) {
            admin.forEach(value => {
                aux.push(value);
            });
        }

        if (authorities.find(authority => authority.roleCode === "MANAGER") !== undefined) {
            manager.forEach(value => {
                aux.push(value);
            });
        }

        if (authorities.find(authority => authority.roleCode === "USER") !== undefined) {
            user.forEach(value => {
                aux.push(value);
            });
        }

        setComponent(menu(aux, userData));
    }

    /**
     * @description menu component
     * @param value menu items
     * @param userData user data
     */
    function menu(value, userData) {
        return <Menu userData={userData}
                     icon={<MdCloudCircle className={"text-color-aux font-size-50"}/>}
                     title={"Payroll"}
                     text={value[0].text}
                     component={value[0].component}
                     contents={
                         value
                     }>
        </Menu>
    }

    /**
     * @description: Set initial state
     */
    useEffect(() => {
        try {
            if (localStorage.getItem("USER_KEY") === null
                || localStorage.getItem("USER_ID") === null) {
                // @ts-ignore
                localStorage.setItem("USER_KEY", location.state.token);
                // @ts-ignore
                localStorage.setItem("USER_ID", location.state.userId);
            }

            setComponent(<ChargingPad title={"Welcome"} description={"We are recovering your information"}/>);//component initial

            // @ts-ignore
            load(Number(localStorage.getItem("USER_ID"))).then(r => {
                setTimeout(() => {
                    assignFeatures(r);// dashboard component
                }, 1500);
            }).catch(error => {
                if (error.response.status === 401) {
                    error401();
                }
            });
        } catch (error) {
            localStorage.clear();
            navigate("/login");
        }
    }, []);

    return (
        <Fragment>
            {component}
            <Modal show={showModal} close={() => setShowModal(false)}
                   content={contentModal} isClose={isCloseModal}/>
        </Fragment>
    )
}
export default Dashboard;
