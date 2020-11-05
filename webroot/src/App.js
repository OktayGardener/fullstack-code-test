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

  addItemToState = (item) => {
  	this.getItems()
    this.setState(prevState => ({
      items: this.items
    }))
  }

  updateState = (item) => {
    const itemIndex = this.state.items.findIndex(data => data.id === item.id)
    const newArray = [
    // destructure all items from beginning to the indexed item
      ...this.state.items.slice(0, itemIndex),
    // add the updated item to the array
      item,
    // add the rest of the items to the array from the index after the replaced item
      ...this.state.items.slice(itemIndex + 1)
    ]
    this.setState({ items: newArray })
  }

  deleteItemFromState = (name) => {
  	this.getItems()
//    const updatedItems = this.state.items.filter(item => item.name !== name)
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
