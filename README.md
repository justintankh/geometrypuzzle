# Geometry Puzzle Deployment Repository

Welcome to the GIC assessment Geometry Puzzle Repository! This repository serves as the hub for the deployment of the
live demo, documentation, and source code of the coding assessment. Here, you will find the Backend code and the
complete logic of the assessment.

## Project Overview

In this coding assessment, I took a focused approach to develop a full-stack chat bot with a scatter plot overlay for visualizing shape building activities. The goal was to create an engaging and interactive experience for users. The key details of the project are as follows:

- Core functionality: The Java Springboot backend code contains the core functionality and logic of the Geometry Puzzle. The backend code is responsible for handling data processing, executing algorithms, and managing chatbot interactions.

- Frontend Presentation: The frontend layer of the project was built using Typescript React. It serves as the presentation layer, providing users with an intuitive interface to interact with the Geometry Puzzle.

- Visualization: The frontend code, built with Typescript React, includes a scatter plot overlay that enables users to visualize their shape building activities. This feature enhances the interactive nature of the Geometry Puzzle.

I encourage you to explore the backend code to gain insights into the core functionality and logic of the Geometry Puzzle. Additionally, reviewing the frontend code will give you a better understanding of the user interface and the interactive features it offers.

## Frontend Code

The frontend code for the Geometry Puzzle can be accessed
at [geometrypuzzle_web](https://github.com/justintankh/geometrypuzzle_web). This repository contains the client-side
code responsible for the user interface and interaction with the Geometry Puzzle.

## Live Demo

To experience the Geometry Puzzle in action, we have provided a full live demo. You can access it by
visiting [https://justintankh.github.io/geometrypuzzle_web/](https://justintankh.github.io/geometrypuzzle_web/). This
demo showcases the features and functionality of the Geometry Puzzle, giving you an opportunity to explore its
capabilities firsthand.

We encourage you to check out the live demo and explore the various puzzles and challenges available. The Geometry
Puzzle is an engaging way to test your spatial reasoning and problem-solving skills.

- If you experience slowness in the demo, it is most likely because the services are in a domant state.

## Getting Started

If you are interested in diving into the source code and exploring the backend logic of the Geometry Puzzle. Follow the
steps below to get started:

1. Have Maven, Docker and Java 17 installed.
2. Clone this repository to your local machine.
3. Navigate to the project directory.
4. Run `docker-compose up -d` followed by `mvn clean install`
5. Run `mvn spring-boot:run` to finally get running !

## Getting Started ( Frontend )

To get the frontend started, please download the repository
at [geometrypuzzle_web](https://github.com/justintankh/geometrypuzzle_web)

1. Have Node package manager installed
2. Clone the above repository to your local machine.
3. Navigate to the project directory.
4. Run `npm i && npm run dev`
5. Once vite starts, the project should open up in your window

Feel free to contribute to this repository by submitting pull requests with any improvements, bug fixes, or additional
features you'd like to propose.

## Testing

For testing purposes of the algorithm, existing JUnit test cases support input from CSV files, with the Colon character
acting as a delimiter between commas. In the `test/resources` directory, you will find two sets of prepared convex
dataset files named convexTestTrue.csv and convexTestFalse.csv.

To run the tests, you can utilize the provided datasets or create your own CSV files following the same format.

## Issues and Feedback

If you encounter any issues, have questions, or would like to provide feedback, please feel free to contact me at my
email justintankh@gmail.com

Thank you for your interest in the Geometry Puzzle! Hope you enjoy the live demo, find the source code informative, and
have a great time coding !
