import 'package:flutter/material.dart';
import 'package:web_socket_channel/io.dart';
import 'package:web_socket_channel/web_socket_channel.dart';
import 'package:web_socket_channel/html.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
  TextEditingController _controller = TextEditingController();
  List<String> _list = [];
  HtmlWebSocketChannel _channel =
      HtmlWebSocketChannel.connect("ws://192.168.1.3:8080/ws");

  void _incrementCounter() {
    setState(() {
      _list.add("send -> ${_controller.text}");
      _channel.sink.add(_controller.text);
    });
  }

  void _connect() {
    _channel.stream.listen((event) {
      print(event.toString());
      _list.add(event.toString());
      setState(() {});
    });
  }

  @override
  void initState() {
    // super.initState();
    _connect();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Column(
        children: [
          Container(
            width: 400,
            height: 400,
            child: ListView.builder(
              itemBuilder: (context, index) {
                return SelectableText(
                  "${_list[index]}",
                );
              },
              itemCount: _list.length,
            ),
          ),
          Container(
            width: 400,
            height: 100,
            child: TextField(
              controller: _controller,
            ),
          )
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Send',
        child: const Icon(Icons.send),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
