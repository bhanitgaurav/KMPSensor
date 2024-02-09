import SwiftUI
import shared
import CoreMotion
import UIKit
struct ContentView: View {
	let greet = Greeting().greet()

	var body: some View {
//		Text(greet)
        MotionView(stringValue:greet)
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}

struct MotionView: UIViewControllerRepresentable {
    
    let stringValue: String
    
    let manager = CMMotionManager()
    let circle = UIView(frame: CGRect(x: UIScreen.main.bounds.width / 2 - 50,
                                      y: UIScreen.main.bounds.height / 2 - 50,
                                      width: 100,
                                      height: 100))
    
    func makeUIViewController(context: Context) -> UIViewController {
        let viewController = UIViewController()
        circle.layer.cornerRadius = 50
        circle.layer.backgroundColor = UIColor.systemGreen.cgColor
        viewController.view.addSubview(circle)
        return viewController
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        if manager.isAccelerometerAvailable {
            manager.accelerometerUpdateInterval = 1 / 60.0
            manager.startAccelerometerUpdates(to: .main) { (data, error) in
                guard error == nil else { return }
                guard let accelerometerData = data else { return }
                
                DispatchQueue.main.async {
                    self.circle.center = CGPoint(
                        x: self.circle.center.x + accelerometerData.acceleration.x,
                        y: self.circle.center.y + accelerometerData.acceleration.y
                    )
                }
            }
        }
    }
}
