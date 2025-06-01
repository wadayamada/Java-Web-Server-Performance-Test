#[derive(Debug)]

fn main() {
    println!("This is a sample code snippet.");
}

fn person_function() {
    print_wada();
    let p = return_yamada();
    println!("{:?}", p);
}

fn print_wada() {
    let wada = Person {
        name: "Wada".to_string(),
        age: 26,
    };
    println!("{:?}", wada);
}

fn return_yamada() -> Person {
    let yamada = Person {
        name: "Yamada".to_string(),
        age: 22,
    };
    yamada
}

struct Person {
    name: String,
    age: i32,
}
