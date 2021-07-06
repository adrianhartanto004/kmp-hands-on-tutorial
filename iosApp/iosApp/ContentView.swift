import SwiftUI
import shared

struct ContentView: View {
  @ObservedObject var viewModel: ContentViewModel

  init(viewModel: ContentViewModel) {
    self.viewModel = viewModel
  }

  var body: some View {
    NavigationView {
      listView()
        .navigationBarTitle("SpaceX Launches")
        .navigationBarItems(trailing:
                              Button("Reload") {
                                self.viewModel.loadLaunches(forceReload: true)
                              })
    }
  }

  private func listView() -> AnyView {
    switch viewModel.launches {
    case .loading:
      return AnyView(Text("Loading...").multilineTextAlignment(.center))
    case .result(let launches):
      return AnyView(List(launches) { launch in
        RocketLaunchRow(rocketLaunch: launch)
      })
    case .error(let description):
      return AnyView(Text(description).multilineTextAlignment(.center))
    }
  }
}

extension RocketLaunch: Identifiable { }
