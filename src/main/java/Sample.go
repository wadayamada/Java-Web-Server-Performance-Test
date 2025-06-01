package main

import "fmt"

func main() {
	fmt.Println("This is a sample code snippet.")
}

func personFunction() {
	printWada()
	p := returnYamada()
	fmt.Println(p)
}

func printWada() {
	wada := &Person{"Wada", 26}
	fmt.Println(wada)
}

func returnYamada() *Person {
	yamada := &Person{"Yamada", 22}
	return yamada
}

type Person struct {
	name string
	age  int
}
