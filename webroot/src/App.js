import React, { Component } from 'react'
import { Container, Row, Col } from 'reactstrap'
import ModalForm from './Components/Modals/Modal'
import DataTable from './Components/Tables/DataTable'
import { CSVLink } from "react-csv"

class App extends Component {
  state = {
    items: []
  }

  getItems(){
    fetch('/service')
      .then(response => response.json())
      .then(items => this.setState({items}))
      .then(items => console.log(items))
      .catch(err => console.log(err))
  }

  statusNone(){
  	if(this.items[0].status == "") {
		return true
  	}
  	return false
  }

  addItemToState = (item) => {
  	this.getItems()
    this.setState(prevState => ({
      items: this.items
    }))
    window.location.reload(false);
  }

  updateState = (item) => {
	console.log(item)
    this.getItems()
    this.setState({ items: this.items })
    window.location.reload(false);
  }

  deleteItemFromState = (name) => {
  	while (this.statusNone) {
  	this.getItems()
  	window.location.reload(false);
  	console.log('getting items')
  	}

    this.setState({ items: this.state.items })
  }

  componentDidMount(){
    this.getItems()
    setInterval(this.getItems, 5000);
  }

  render() {
    return (
      <Container className="App">
        <Row>
          <Col>
            <h1 style={{margin: "20px 0"}}>KRY Service Poller</h1>
          </Col>
        </Row>
        <Row>
          <Col>
            <DataTable items={this.state.items} updateState={this.updateState} deleteItemFromState={this.deleteItemFromState} />
          </Col>
        </Row>
        <Row>
          <Col>
            <ModalForm buttonLabel="Add Item" addItemToState={this.addItemToState}/>
          </Col>
        </Row>
      </Container>
    )
  }
}

export default App
